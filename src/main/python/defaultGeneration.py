from defaultTables import CLAY_CONTENT_PER_COUNTRY, SOIL_CARBON_CONTENT_PER_COUNTRY,\
    ROOTING_DEPTH_PER_CROP, FERT_N_RATIO_PER_COUNTRY, FERT_P_RATIO_PER_COUNTRY, FERT_K_RATIO_PER_COUNTRY,\
    MANURE_LIQUID_RATIO_PER_COUNTRY, MANURE_SOLID_RATIO_PER_COUNTRY,\
    ANNUAL_PRECIPITATION_PER_COUNTRY, YEARLY_PRECIPITATION_AS_SNOW_PER_COUNTRY,\
    WATER_CONTENT_FM_RATIO_PER_CROP, IRR_TECH_RATIO_PER_COUNTRY,\
    LAND_USE_CATEGORY_PER_CROP, CROP_FACTOR_PER_CROP, SAND_CONTENT_PER_COUNTRY,\
    SOIL_ERODIBILITY_FACTOR_PER_SOIL_TEXTURE,\
    ENERGY_GROSS_CALORIFIC_VALUE_PER_CROP_PARTIAL,\
    SOIL_WITH_PH_UNDER_OR_7_PER_COUNTRY, SEED_TYPE_IP_PER_CROP,\
    SEED_TYPE_ORG_PER_CROP, CARBON_CONTENT_PER_CROP
from models.hmmodel import LandUseCategoryForHM, PesticideType
from models.otherorganicfertilisermodel import OtherOrganicFertiliserType
from models.pmodel import LandUseCategory
from models.fertilisermodel import OtherMineralFertiliserType
from models.seedmodel import SeedType
from models.erosionmodel import TillageMethod, AntiErosionPractice
from models.modelEnums import SoilTexture
from models.atomicmass import MA_CO2, MA_C
from defaultMatrixYieldPerYear import YIELD_PER_YEAR_PER_CROP_PER_COUNTRY
from defaultMatrixTotalMineralFert import NITROGEN_FROM_MINERAL_FERT_PER_CROP_PER_COUNTRY
from defaultMatrixNUptake import NITROGEN_UPTAKE_PER_CROP_PER_COUNTRY
from defaultMatrixTotalManure import TOTAL_MANURE_LIQUID_PER_CROP_PER_COUNTRY,\
    TOTAL_MANURE_SOLID_PER_CROP_PER_COUNTRY
from defaultMatrixSeed import NB_SEEDS_PER_PARTIAL_CROP_PER_COUNTRY,\
    NB_PLANTED_TREES_PER_PARTIAL_CROP_PER_COUNTRY

class DefaultValuesWrapper(object):
    def __init__(self, inputMapping, generatorMap):
        self.inputMapping = inputMapping
        self._generatorMap = generatorMap
        
    #TODO: Cache the generated values
    def __getattr__(self, name):
        try:
            res = self.inputMapping[name]
        except KeyError:
            res = self._generatorMap[name].generateDefault(name, self)
        return res;
    
    def __getitem__(self, name):
        return self.__getattr__(name)
    
    def __contains__(self, name):
        return name in self.inputMapping or name in self._generatorMap

#FIXME: Why is it not used?
class NotFoundGenerator(object):
    def generateDefault(self, field, generators):
        raise "No generator found for field " + field

class SimpleValueDefaultGenerator(object):
    def __init__(self, value):
        self._value = value
        
    def generateDefault(self, field, generators):
        return self._value
    
class TableLookupDefaultGenerator(object):
    def __init__(self, keyField, table):
        self._keyField = keyField
        self._table = table
        
    def generateDefault(self, field, generators):
        return self._table[generators[self._keyField]]
    
class ZeroMapDefaultGenerator(object):
    def __init__(self, enumClass):
        self._enumClass = enumClass
    
    def generateDefault(self, attrName, mapping):
        return {k: 0.0 for k in self._enumClass}

class ConvertRatioToValueDefaultGenerator(object):
    def __init__(self, tableKeyField, ratioTable, totalField):
        self._tableKeyField = tableKeyField
        self._ratioTable = ratioTable
        self._totalField = totalField
        
    def generateDefault(self, field, generators):
        total = generators[self._totalField]
        return {k:v * total for k,v in self._ratioTable[generators[self._tableKeyField]].items()}
    
class CropCyclePerYearDefaultGenerator(object):
    def generateDefault(self, field, generators):
        return 1.0 #FIXME Implement
    
class AnnualizedIrrigationDefaultGenerator(object):
    def generateDefault(self, field, generators): #m3/(ha*crop cycle) -> mm/year
        return generators["water_use_total"] * 0.1 * generators["crop_cycle_per_year"]
    
class LandUseCategoryForHMDefaultGenerator(object):
    def generateDefault(self, field, generators):
        if (generators["land_use_category"] == LandUseCategory.arable_land):
            return LandUseCategoryForHM.arable_land
        elif (generators["land_use_category"] in [LandUseCategory.grassland_intensive,
                                                     LandUseCategory.grassland_intensive,
                                                     LandUseCategory.summer_alpine_pastures]):
            return LandUseCategoryForHM.permanent_grassland
        else:
            return LandUseCategoryForHM.horticultural_crops
        
class SoilTextureDefaultGenerator(object):
    def generateDefault(self, field, generators):
        clay_content = generators["clay_content"]
        sand_content = generators["sand_content"]
        if (clay_content < 0.18 and sand_content > 0.65):
            return SoilTexture.coarse
        elif ((0.18 < clay_content and clay_content < 0.35 and sand_content > 0.15)
              or (clay_content < 0.18 and 0.15 < sand_content and sand_content < 0.65)):
            return SoilTexture.medium
        elif (clay_content < 0.35 and sand_content < 0.15):
            return SoilTexture.medium_fine
        elif (0.35 < clay_content  and clay_content  < 0.60):
            return SoilTexture.fine
        elif (clay_content > 0.60):
            return SoilTexture.very_fine
        else:
            return SoilTexture.unknown

class PerCropCyclePrecipitationDefaultGenerator(object):
    def generateDefault(self, field, generators): #mm/year -> m3/(ha*crop cycle)
        return generators["average_annual_precipitation"] * 10.0 / generators["crop_cycle_per_year"]
    
class PerCropCycleYieldDefaultGenerator(object):
    def generateDefault(self, field, generators):
        return generators["yield_main_product_per_year"] / generators["crop_cycle_per_year"]
   
class DryingDefaultGenerator(object):    
    def generateDefault(self, field, generators):
        if ("drying_yield_to_be_dryied" in generators
            and "drying_humidity_before_drying" in generators
            and "drying_humidity_after_drying" in generators):
            return generators["yield_main_product_per_crop_cycle"] \
                * generators["drying_yield_to_be_dryied"] \
                * (generators["drying_humidity_before_drying"] - generators["drying_humidity_after_drying"])
        else: return 0.0
        
class SlopePerCropGenerator(object):
    def generateDefault(self, field, generators):
        if (generators["crop"] == "rice"):
            return 0.00
        else: return 0.03
    
class CO2FromYieldGenerator(object):
    def generateDefault(self, field, generators):
        return generators["yield_main_product_carbon_content"] * 1000.0 * MA_CO2/MA_C * (1 - generators["yield_main_product_water_content"])

class EnergyGrossCalorificValueGenerator(object):
    def generateDefault(self, field, generators):
        if (generators["crop"] in ENERGY_GROSS_CALORIFIC_VALUE_PER_CROP_PARTIAL):
            return ENERGY_GROSS_CALORIFIC_VALUE_PER_CROP_PARTIAL[generators["crop"]] * (1 - generators["yield_main_product_water_content"])
        else:
            return generators["CO2_from_yield"] * 11.5
        
class SeedTypeDefaultGenerator(object):
    def generateDefault(self, field, generators):
        if (generators["farming_type"] == "non_organic"):
            return generators["seed_type_ip"]
        else:
            return generators["seed_type_org"]
        
class SeedQuantitiesDefaultGenerator(object):
    def generateDefault(self, field, generators):
        #FIXME: What to do with nb_seedlings?
        if (generators["seed_type"] == SeedType.tree_seedlings_ip or generators["seed_type"] == SeedType.tree_seedlings_org):
            return {generators["seed_type"]:generators["nb_planted_trees"]}
        else:
            return {generators["seed_type"]:generators["seeds"]}
        
class CropCountryMatrixLookupDefaultGenerator(object):
    def __init__(self, table):
        self._table = table
        
    def generateDefault(self, field, generators):
        currentCropTable = self._table[generators["crop"]]
        if (generators["country"] in currentCropTable):
            return currentCropTable[generators["country"]]
        else:
            return currentCropTable["GLO"]

DEFAULTS_VALUES_GENERATORS = {
                   #Cross-models defaults
                   "average_annual_precipitation": TableLookupDefaultGenerator("country", ANNUAL_PRECIPITATION_PER_COUNTRY),
                   "clay_content": TableLookupDefaultGenerator("country", CLAY_CONTENT_PER_COUNTRY),
                   "crop_cycle_per_year": CropCyclePerYearDefaultGenerator(),
                   "farming_type": SimpleValueDefaultGenerator("non_organic"),
                   "precipitation_per_crop_cycle": PerCropCyclePrecipitationDefaultGenerator(),
                   "water_use_total": SimpleValueDefaultGenerator(0.0), #FIXME: to calculate
                   "yield_main_product_per_year":CropCountryMatrixLookupDefaultGenerator(YIELD_PER_YEAR_PER_CROP_PER_COUNTRY),
                   "yield_main_product_per_crop_cycle": PerCropCycleYieldDefaultGenerator(),
                   
                   #Fertiliser defaults
                   "nitrogen_from_mineral_fert": CropCountryMatrixLookupDefaultGenerator(NITROGEN_FROM_MINERAL_FERT_PER_CROP_PER_COUNTRY), #FIXME: no values for mint and pineapple
                   "p2O5_from_mineral_fert": SimpleValueDefaultGenerator(0.0), #FIXME: missing Default (crop+country)
                   "k2O_from_mineral_fert": SimpleValueDefaultGenerator(0.0), #FIXME: missing Default (crop+country)
                   "n_fertiliser_quantities": ConvertRatioToValueDefaultGenerator("country", FERT_N_RATIO_PER_COUNTRY, "nitrogen_from_mineral_fert"),
                   "p_fertiliser_quantities": ConvertRatioToValueDefaultGenerator("country", FERT_P_RATIO_PER_COUNTRY, "p2O5_from_mineral_fert"),
                   "k_fertiliser_quantities": ConvertRatioToValueDefaultGenerator("country", FERT_K_RATIO_PER_COUNTRY, "k2O_from_mineral_fert"),
                   "other_mineral_fertiliser_quantities": ZeroMapDefaultGenerator(OtherMineralFertiliserType),
                   "soil_with_ph_under_or_7": TableLookupDefaultGenerator("country", SOIL_WITH_PH_UNDER_OR_7_PER_COUNTRY),
                   #Manure defaults
                   "liquid_manure_part_before_dilution": SimpleValueDefaultGenerator(0.5),
                   "total_manureliquid":CropCountryMatrixLookupDefaultGenerator(TOTAL_MANURE_LIQUID_PER_CROP_PER_COUNTRY), #FIXME: no values for mint and pineapple
                   "total_manuresolid":CropCountryMatrixLookupDefaultGenerator(TOTAL_MANURE_SOLID_PER_CROP_PER_COUNTRY), #FIXME: no values for mint and pineapple
                   "liquid_manure_quantities": ConvertRatioToValueDefaultGenerator("country", MANURE_LIQUID_RATIO_PER_COUNTRY, "total_manureliquid"),
                   "solid_manure_quantities": ConvertRatioToValueDefaultGenerator("country", MANURE_SOLID_RATIO_PER_COUNTRY, "total_manuresolid"),
                   #Other organic fertilisers defaults
                   "other_organic_fertiliser_quantities": ZeroMapDefaultGenerator(OtherOrganicFertiliserType),
                   #Seed defaults
                   "seed_type_ip": TableLookupDefaultGenerator("crop", SEED_TYPE_IP_PER_CROP),
                   "seed_type_org": TableLookupDefaultGenerator("crop", SEED_TYPE_ORG_PER_CROP),
                   "seed_type": SeedTypeDefaultGenerator(),
                   "seeds": CropCountryMatrixLookupDefaultGenerator(NB_SEEDS_PER_PARTIAL_CROP_PER_COUNTRY),#FIXME: missing some defaults, some strange values (zero)
                   "nb_seedlings": SimpleValueDefaultGenerator(0.0),#FIXME: no values?
                   "nb_planted_trees": CropCountryMatrixLookupDefaultGenerator(NB_PLANTED_TREES_PER_PARTIAL_CROP_PER_COUNTRY),#FIXME: lifetime planted trees, how to normalize? #FIXME: we have some values for non tree_seedlings crop: coffee,cocoa,tea
                   "seed_quantities": SeedQuantitiesDefaultGenerator(),
                   #Erosion defaults
                   "yearly_precipitation_as_snow": TableLookupDefaultGenerator("country", YEARLY_PRECIPITATION_AS_SNOW_PER_COUNTRY),
                   "annualized_irrigation": AnnualizedIrrigationDefaultGenerator(),
                   "slope": SlopePerCropGenerator(),
                   "slope_length": SimpleValueDefaultGenerator(50.0),
                   "tillage_method": SimpleValueDefaultGenerator(TillageMethod.unknown),
                   "anti_erosion_practice": SimpleValueDefaultGenerator(AntiErosionPractice.unknown),
                   "sand_content": TableLookupDefaultGenerator("country", SAND_CONTENT_PER_COUNTRY),
                   "soil_texture": SoilTextureDefaultGenerator(), 
                   "soil_erodibility_factor": TableLookupDefaultGenerator("soil_texture", SOIL_ERODIBILITY_FACTOR_PER_SOIL_TEXTURE),
                   "crop_factor": TableLookupDefaultGenerator("crop", CROP_FACTOR_PER_CROP),
                   #CO2 model defaults
                   "part_of_urea_in_UAN": SimpleValueDefaultGenerator(0.5),
                   "magnesium_from_fertilizer": SimpleValueDefaultGenerator(0.0),
                   "magnesium_as_dolomite": SimpleValueDefaultGenerator(1.0),
                   #Irrigation defaults
                   "irrigation_types_proportions": TableLookupDefaultGenerator("country", IRR_TECH_RATIO_PER_COUNTRY),
                   #N model defaults
                   "bulk_density_of_soil": SimpleValueDefaultGenerator(1300.0),
                   "c_per_n_ratio": SimpleValueDefaultGenerator(11.0),
                   "considered_soil_volume": SimpleValueDefaultGenerator(5000.0),
                   "drained_part": SimpleValueDefaultGenerator(0.0),
                   "organic_carbon_content": TableLookupDefaultGenerator("country", SOIL_CARBON_CONTENT_PER_COUNTRY),
                   "nitrogen_uptake_by_crop": CropCountryMatrixLookupDefaultGenerator(NITROGEN_UPTAKE_PER_CROP_PER_COUNTRY), #FIXME: no values for mint and pineapple
                   "norg_per_ntotal_ratio": SimpleValueDefaultGenerator(0.85),
                   "rooting_depth": TableLookupDefaultGenerator("crop", ROOTING_DEPTH_PER_CROP), #FIXME: Missing some default
                   "nitrogen_from_crop_residues": SimpleValueDefaultGenerator(0.0), #FIXME: Default
                   #P defaults
                   "eroded_reaching_river": SimpleValueDefaultGenerator(0.2),
                   "eroded_soil_p_enrichment": SimpleValueDefaultGenerator(1.86),
                   "p_content_in_soil": SimpleValueDefaultGenerator(0.00095),
                   "land_use_category": TableLookupDefaultGenerator("crop", LAND_USE_CATEGORY_PER_CROP), #FIXME: some defaults to be verified
                   #HM defaults
                   "hm_land_use_category": LandUseCategoryForHMDefaultGenerator(),
                   "pest_total": SimpleValueDefaultGenerator(0),#FIXME: Default (crop+country) GD_crop Pesticides_L1, no values for:Apricot,Asparagus,Carrot,Coconut,Lemon,Mint,Oat,Olive,Onion,Pear,Pineapple,Strawberry,SweetCorn,Tea
                   "pesticides_quantities": ZeroMapDefaultGenerator(PesticideType),#FIXME: Default
                   #Packaging rules
                   "ca_from_mineral_fert": SimpleValueDefaultGenerator(0),
                   #Direct outputs
                   "computed_drying":DryingDefaultGenerator(),
                   "type_of_drying":SimpleValueDefaultGenerator("ambient_air"),
                   "yield_main_product_water_content": TableLookupDefaultGenerator("crop", WATER_CONTENT_FM_RATIO_PER_CROP),
                   "yield_main_product_carbon_content": TableLookupDefaultGenerator("crop", CARBON_CONTENT_PER_CROP),
                   "CO2_from_yield": CO2FromYieldGenerator(),
                   "energy_gross_calorific_value": EnergyGrossCalorificValueGenerator()
                   }
