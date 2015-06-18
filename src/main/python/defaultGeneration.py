from defaultTables import CLAY_CONTENT_PER_COUNTRY, SOIL_CARBON_CONTENT_PER_COUNTRY,\
    ROOTING_DEPTH_PER_CROP, FERT_N_RATIO_PER_COUNTRY, FERT_P_RATIO_PER_COUNTRY, FERT_K_RATIO_PER_COUNTRY,\
    MANURE_LIQUID_RATIO_PER_COUNTRY, MANURE_SOLID_RATIO_PER_COUNTRY,\
    ANNUAL_PRECIPITATION_PER_COUNTRY, YEARLY_PRECIPITATION_AS_SNOW_PER_COUNTRY,\
    WATER_CONTENT_FM_RATIO_PER_CROP, IRR_TECH_RATIO_PER_COUNTRY,\
    LAND_USE_CATEGORY_PER_CROP, CROP_FACTOR_PER_CROP, SAND_CONTENT_PER_COUNTRY,\
    SOIL_ERODIBILITY_FACTOR_PER_SOIL_TEXTURE,\
    ENERGY_GROSS_CALORIFIC_VALUE_PER_CROP_PARTIAL,\
    SOIL_WITH_PH_UNDER_OR_7_PER_COUNTRY, CARBON_CONTENT_PER_CROP, IRR_WATERUSE_RATIO_PER_COUNTRY
from models.hmmodel import LandUseCategoryForHM, PesticideType
from models.otherorganicfertilisermodel import CompostType, SludgeType
from models.pmodel import LandUseCategory
from models.fertilisermodel import OtherMineralFertiliserType
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
from datetime import date
import dateutil.relativedelta as relativedelta
from directMappingEnums import PlasticDisposal, Plantprotection, Soilcultivation,\
    Sowingplanting, Fertilisation, Harvesting, OtherWorkProcesses

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
    
class CountryMatrixLookupDefaultGenerator(object):
    def __init__(self, keyField, table):
        self._keyField = keyField
        self._table = table
        
    def generateDefault(self, field, generators):
        if (generators["country"] in self._table):
            currentCountryTable = self._table[generators["country"]]
        else:
            currentCountryTable = self._table["GLO"]
        return currentCountryTable[self._keyField];
    
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
    
class RatiosToValuesConvertor(object):
    def __init__(self, ratiosField, totalField):
        self._ratiosField = ratiosField
        self._totalField = totalField
        
    def generateDefault(self, field, generators):
        total = generators[self._totalField]
        return {k:v * total for k,v in generators[self._ratiosField].items()}
    
class OneRatioToValueConvertor(object):
    def __init__(self, ratioField, totalField):
        self._ratioField = ratioField
        self._totalField = totalField
        
    def generateDefault(self, field, generators):
        return generators[self._ratioField] *generators[self._totalField]
    
class CropCyclePerYearDefaultGenerator(object):
    def generateDefault(self, field, generators):
        try:
            previousHarvestDate = generators["harvest_date_previous_crop"];
            previousHarvestDate = date(previousHarvestDate[0], previousHarvestDate[1], previousHarvestDate[2]);
            harvestDate = generators["harvesting_date_main_crop"];
            harvestDate = date(harvestDate[0], harvestDate[1], harvestDate[2]);
            dateDiff = relativedelta.relativedelta(harvestDate, previousHarvestDate);
            #If remaining days are more than 4 weeks, consider a month, if more than 2 weeks, consider half a month 
            return 1.0 / (dateDiff.years + dateDiff.months/12.0 + (dateDiff.days // 14.0) / 24.0);
        except KeyError:
            return 1.0;
    
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
        
class SlopePerCropGenerator(object):
    def generateDefault(self, field, generators):
        if (generators["crop"] == "rice"):
            return 0.00
        else: return 0.03
        
class DryContentGenerator(object):
    def generateDefault(self, field, generators):
        return 1 - generators["yield_main_product_water_content"]
    
class DryYieldGenerator(object):
    def generateDefault(self, field, generators):
        generators["yield_main_product_dry_content"] * generators["yield_main_product_per_crop_cycle"]
    
class CO2FromYieldGenerator(object):
    def generateDefault(self, field, generators):
        return generators["yield_main_product_carbon_content"] * MA_CO2/MA_C * generators["yield_main_product_dry_per_crop_cycle"]

class EnergyGrossCalorificValueGenerator(object):
    def generateDefault(self, field, generators):
        if (generators["crop"] in ENERGY_GROSS_CALORIFIC_VALUE_PER_CROP_PARTIAL):
            return ENERGY_GROSS_CALORIFIC_VALUE_PER_CROP_PARTIAL[generators["crop"]] * generators["yield_main_product_dry_per_crop_cycle"]
        else:
            return generators["CO2_from_yield"] * 11.5
        
class EolPlasticDisposalGenerator(object):
    def generateDefault(self, field, generators):
        return generators["materials_fleece"] \
             + generators["materials_silage_foil"] \
             + generators["materials_covering_sheet"]\
             + generators["materials_bird_net"];
        
class SeedQuantitiesDefaultGenerator(object):
    def generateDefault(self, field, generators):
        crop = generators["crop"]
        if ( crop in ["almond", "apple", "apricot", "banana", "cocoa", "coconut", "coffee", "lemonlime", "mandarin", "olive", "orange", "palmtree", "peach", "pear", "pineapple", "sugarcane", "tea"]):
            value_field = "nb_planted_trees"
        elif (crop in ["asparagus", "mint", "onion", "strawberry", "tomato"]):
            value_field = "nb_seedlings"
        else:
            value_field = "seeds"
        try:
            return {crop: generators[value_field]}
        except KeyError:
            return {crop: 0.0}
        
class CropCountryMatrixLookupDefaultGenerator(object):
    def __init__(self, table):
        self._table = table
        
    def generateDefault(self, field, generators):
        currentCropTable = self._table[generators["crop"]]
        if (generators["country"] in currentCropTable):
            return currentCropTable[generators["country"]]
        else:
            return currentCropTable["GLO"]
        
class FlatRatioRepartitionGenerator(object):
    def __init__(self, enumClass):
        self._enumClass = enumClass
    
    def generateDefault(self, field, generators):
        return {k: 1.0 / self._enumClass.len() for k in self._enumClass}
    
class NitrogenFromCropResiduesDefaultGenerator(object):
    def generateDefault(self, field, generators):
        crop = generators["crop"]
        if (crop == "carrot"):#(YieldDM * slope + intercept*1000) * (Nag*(1-FracRemove)+Rbg-bio*Nbg)
            return (generators["yield_main_product_dry_per_crop_cycle"] * 1.07 + 1540.0) * (0.016+0.2*0.014)
        elif (crop == "linseed"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 1.13 + 850.0) * (0.008+0.19*0.008)
        elif (crop == "maizegrain"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 1.03 + 610.0) * (0.006+0.22*0.007)
        elif (crop == "oat"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 0.91 + 890.0) * (0.007*0.8+0.25*0.008)
        elif (crop == "peanut"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 1.07 + 1540.0) * (0.016+0.2*0.014)
        elif (crop == "potato"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 0.1 + 1060.0) * (0.019+0.2*0.014)
        elif (crop == "rapeseed"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 1.5) * (0.011+0.19*0.017)
        elif (crop == "rice"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 0.95 + 2460.0) * (0.007)
        elif (crop == "soybean"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 0.93 + 1350.0) * (0.008+0.19*0.008)
        elif (crop == "sweetcorn"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 1.03 + 610.0) * (0.006+0.22*0.007)
        elif (crop == "wheat"):
            return (generators["yield_main_product_dry_per_crop_cycle"] * 1.61 + 400.0) * (0.006*0.8+0.23*0.009)
        #Other formula
        elif (crop == "sugarbeet"):
            return generators["yield_main_product_dry_per_crop_cycle"] * (0.5*0.016 + 0.2*0.014)
        elif (crop == "sugarcane"):
            return generators["yield_main_product_dry_per_crop_cycle"] / 6.0 * 0.43 * 0.004 
        elif (crop == "coconut"):
            return 44.0
        elif (crop == "palmtree"):
            return 159.0
        else:
            return 0.0


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
                   "yield_main_product_dry_per_crop_cycle": DryYieldGenerator(),
                   
                   #Fertiliser defaults
                   "nitrogen_from_mineral_fert": CropCountryMatrixLookupDefaultGenerator(NITROGEN_FROM_MINERAL_FERT_PER_CROP_PER_COUNTRY),
                   "p2O5_from_mineral_fert": SimpleValueDefaultGenerator(0.0), #NOTE: No default for now, maybe in fertistat
                   "k2O_from_mineral_fert": SimpleValueDefaultGenerator(0.0), #NOTE: No default for now, maybe in fertistat
                   "n_fertiliser_proportions": TableLookupDefaultGenerator("country", FERT_N_RATIO_PER_COUNTRY),
                   "p_fertiliser_proportions": TableLookupDefaultGenerator("country", FERT_P_RATIO_PER_COUNTRY),
                   "k_fertiliser_proportions": TableLookupDefaultGenerator("country", FERT_K_RATIO_PER_COUNTRY),
                   "n_fertiliser_quantities": RatiosToValuesConvertor("n_fertiliser_proportions", "nitrogen_from_mineral_fert"),
                   "p_fertiliser_quantities": RatiosToValuesConvertor("p_fertiliser_proportions", "p2O5_from_mineral_fert"),
                   "k_fertiliser_quantities": RatiosToValuesConvertor("k_fertiliser_proportions", "k2O_from_mineral_fert"),
                   "ca_from_mineral_fert": SimpleValueDefaultGenerator(0.0),
                   "other_mineral_fertiliser_proportions": FlatRatioRepartitionGenerator(OtherMineralFertiliserType),
                   "other_mineral_fertiliser_quantities": RatiosToValuesConvertor("other_mineral_fertiliser_proportions", "ca_from_mineral_fert"),
                   "soil_with_ph_under_or_7": TableLookupDefaultGenerator("country", SOIL_WITH_PH_UNDER_OR_7_PER_COUNTRY),
                   #Manure defaults
                   "liquid_manure_part_before_dilution": SimpleValueDefaultGenerator(0.5),
                   "total_manureliquid":CropCountryMatrixLookupDefaultGenerator(TOTAL_MANURE_LIQUID_PER_CROP_PER_COUNTRY),
                   "total_manuresolid":CropCountryMatrixLookupDefaultGenerator(TOTAL_MANURE_SOLID_PER_CROP_PER_COUNTRY),
                   "liquid_manure_proportions": TableLookupDefaultGenerator("country", MANURE_LIQUID_RATIO_PER_COUNTRY),
                   "solid_manure_proportions": TableLookupDefaultGenerator("country", MANURE_SOLID_RATIO_PER_COUNTRY),
                   "liquid_manure_quantities": RatiosToValuesConvertor("liquid_manure_proportions", "total_manureliquid"),
                   "solid_manure_quantities": RatiosToValuesConvertor("solid_manure_proportions", "total_manuresolid"),
                   #Other organic fertilisers defaults
                   "total_composttype":SimpleValueDefaultGenerator(0.0),
                   "total_sewagesludge":SimpleValueDefaultGenerator(0.0),
                   "compost_proportions": FlatRatioRepartitionGenerator(CompostType),
                   "sludge_proportions": FlatRatioRepartitionGenerator(SludgeType),
                   "compost_quantities": RatiosToValuesConvertor("compost_proportions", "total_composttype"),
                   "sludge_quantities": RatiosToValuesConvertor("sludge_proportions", "total_sewagesludge"),
                   #Seed defaults
                   "seeds": CropCountryMatrixLookupDefaultGenerator(NB_SEEDS_PER_PARTIAL_CROP_PER_COUNTRY),
                   "nb_seedlings": CropCountryMatrixLookupDefaultGenerator({}),
                   "nb_planted_trees": CropCountryMatrixLookupDefaultGenerator(NB_PLANTED_TREES_PER_PARTIAL_CROP_PER_COUNTRY),
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
                   "irrigation_types_quantities": RatiosToValuesConvertor("irrigation_types_proportions", "water_use_total"),
                   "irrigation_water_use_proportions": TableLookupDefaultGenerator("country", IRR_WATERUSE_RATIO_PER_COUNTRY),
                   "irrigation_water_use_quantities": RatiosToValuesConvertor("irrigation_water_use_proportions", "water_use_total"),
                   #N model defaults
                   "bulk_density_of_soil": SimpleValueDefaultGenerator(1300.0),
                   "c_per_n_ratio": SimpleValueDefaultGenerator(11.0),
                   "considered_soil_volume": SimpleValueDefaultGenerator(5000.0),
                   "drained_part": SimpleValueDefaultGenerator(0.0),
                   "organic_carbon_content": TableLookupDefaultGenerator("country", SOIL_CARBON_CONTENT_PER_COUNTRY),
                   "nitrogen_uptake_by_crop": CropCountryMatrixLookupDefaultGenerator(NITROGEN_UPTAKE_PER_CROP_PER_COUNTRY),
                   "norg_per_ntotal_ratio": SimpleValueDefaultGenerator(0.85),
                   "rooting_depth": TableLookupDefaultGenerator("crop", ROOTING_DEPTH_PER_CROP),
                   "nitrogen_from_crop_residues": NitrogenFromCropResiduesDefaultGenerator(),
                   #P defaults
                   "eroded_reaching_river": SimpleValueDefaultGenerator(0.2),
                   "eroded_soil_p_enrichment": SimpleValueDefaultGenerator(1.86),
                   "p_content_in_soil": SimpleValueDefaultGenerator(0.00095),
                   "land_use_category": TableLookupDefaultGenerator("crop", LAND_USE_CATEGORY_PER_CROP),
                   #HM defaults
                   "hm_land_use_category": LandUseCategoryForHMDefaultGenerator(),
                   "pest_total": SimpleValueDefaultGenerator(0),#FIXME: Default (crop+country) GD_crop Pesticides_L1, no values for:Apricot,Asparagus,Carrot,Coconut,Lemon,Mint,Oat,Olive,Onion,Pear,Pineapple,Strawberry,SweetCorn,Tea
                   "pesticides_quantities": ZeroMapDefaultGenerator(PesticideType),#FIXME: Default
                   #Packaging rules
                   "ca_from_mineral_fert": SimpleValueDefaultGenerator(0),
                   #Direct outputs
                   "yield_main_product_water_content": TableLookupDefaultGenerator("crop", WATER_CONTENT_FM_RATIO_PER_CROP),
                   "yield_main_product_dry_content": DryContentGenerator(),
                   "yield_main_product_carbon_content": TableLookupDefaultGenerator("crop", CARBON_CONTENT_PER_CROP),
                   "CO2_from_yield": CO2FromYieldGenerator(),
                   "energy_gross_calorific_value": EnergyGrossCalorificValueGenerator(),
                   #---
                   "total_plantprotection": SimpleValueDefaultGenerator(0.0),
                   "total_soilcultivation": SimpleValueDefaultGenerator(0.0),
                   "total_sowingplanting": SimpleValueDefaultGenerator(0.0),
                   "total_fertilisation": SimpleValueDefaultGenerator(0.0),
                   "total_harvesting": SimpleValueDefaultGenerator(0.0),
                   "total_otherworkprocesses": SimpleValueDefaultGenerator(0.0),
                   "plantprotection_proportions": SimpleValueDefaultGenerator({Plantprotection.other: 1.0}),
                   "soilcultivation_proportions": SimpleValueDefaultGenerator({Soilcultivation.other: 1.0}),
                   "sowingplanting_proportions": SimpleValueDefaultGenerator({Sowingplanting.other: 1.0}),
                   "fertilisation_proportions": SimpleValueDefaultGenerator({Fertilisation.other: 1.0}),
                   "harvesting_proportions": SimpleValueDefaultGenerator({Harvesting.other: 1.0}),
                   "otherworkprocesses_proportions": SimpleValueDefaultGenerator({OtherWorkProcesses.other: 1.0}),
                   "plantprotection_quantities": RatiosToValuesConvertor("plantprotection_proportions", "total_plantprotection"),
                   "soilcultivation_quantities": RatiosToValuesConvertor("soilcultivation_proportions", "total_soilcultivation"),
                   "sowingplanting_quantities": RatiosToValuesConvertor("sowingplanting_proportions", "total_sowingplanting"),
                   "fertilisation_quantities": RatiosToValuesConvertor("fertilisation_proportions", "total_fertilisation"),
                   "harvesting_quantities": RatiosToValuesConvertor("harvesting_proportions", "total_harvesting"),
                   "otherworkprocesses_quantities": RatiosToValuesConvertor("otherworkprocesses_proportions", "total_otherworkprocesses"),
                   "materials_fleece": SimpleValueDefaultGenerator(0.0),
                   "materials_silage_foil": SimpleValueDefaultGenerator(0.0),
                   "materials_covering_sheet": SimpleValueDefaultGenerator(0.0),
                   "materials_bird_net": SimpleValueDefaultGenerator(0.0),
                   "total_eol_plastic_disposal_fleece_and_other": EolPlasticDisposalGenerator(),
                   "plastic_disposal_proportions":SimpleValueDefaultGenerator({PlasticDisposal.landfill: 0.5, PlasticDisposal.incineration:0.5}),
                   "plastic_disposal_quantities":RatiosToValuesConvertor("plastic_disposal_proportions", "total_eol_plastic_disposal_fleece_and_other"),
                   "eol_waste_water_to_treatment_facility": SimpleValueDefaultGenerator(0.0),#FIXME: Default?
                   "eol_waste_water_to_nature": SimpleValueDefaultGenerator(0.0),#FIXME: Default?
                   "cod_in_waste_water": SimpleValueDefaultGenerator(0.0)
                   }
                                
