from defaultTables import CLAY_CONTENT_PER_COUNTRY, CARBON_CONTENT_PER_COUNTRY,\
    ROOTING_DEPTH_PER_CROP
from models.hmmodel import LandUseCategoryForHM
from models.otherorganicfertilisermodel import OtherOrganicFertiliserType
from models.pmodel import LandUseCategory

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
    
    def mapField(self, attrName, mapping):
        return {k: 0.0 for k in self._enumClass}
    
class CropCyclePerYearDefaultGenerator(object):
    def generateDefault(self, field, generators):
        return 1.0 #FIXME Implement
    
class AnnualizedIrrigationDefaultGenerator(object):
    def generateDefault(self, field, generators): #m3/(ha*crop cycle) -> mm/year
        return generators["water_use_total"] * 0.1 * generators["crop_cycle_per_year"]
    
class LandUseCategoryForHMDefaultGenerator(object):
    def generateDefault(self, field, generators):
        if (generators["land_use_category"] in (LandUseCategory.arable_land)):
            return LandUseCategoryForHM.arable_land
        elif (generators["land_use_category"] in (LandUseCategory.grassland_intensive, \
                                                     LandUseCategory.grassland_intensive,  \
                                                     LandUseCategory.summer_alpine_pastures)):
            return LandUseCategoryForHM.permanent_grassland
        else:
            return LandUseCategoryForHM.horticultural_crops

DEFAULTS_VALUES_GENERATORS = {
                   #Cross-models defaults
                   "average_annual_precipitation": "TODO",
                   "crop_cycle_per_year": CropCyclePerYearDefaultGenerator(),
                   #Fertiliser defaults
                   #"n_fertiliser_quantities": MapMappingRule(_N_ENUM_TO_FIELD),
                   #"p_fertiliser_quantities": MapMappingRule(_P_ENUM_TO_FIELD),
                   #"k_fertiliser_quantities": MapMappingRule(_K_ENUM_TO_FIELD),
                   #"other_mineral_fertiliser_quantities": MapMappingRule(_OTHERMIN_ENUM_TO_FIELD),
                   "other_organic_fertiliser_quantities": ZeroMapDefaultGenerator(OtherOrganicFertiliserType),#FIXME: To test
                   "soil_with_ph_under_or_7": SimpleValueDefaultGenerator(0.5), #FIXME: Default
                   #Manure defaults
                   "liquid_manure_part_before_dilution": SimpleValueDefaultGenerator(0.5),
                   #Erosion defaults
                   #"yearly_precipitation_as_snow": TODO table
                   "annualized_irrigation": AnnualizedIrrigationDefaultGenerator(),
                   "slope": SimpleValueDefaultGenerator(0.03), #FIXME: default: 0% if rice, 3% for other
                   "slope_length": SimpleValueDefaultGenerator(50.0),
                   "soil_erodibility_factor": SimpleValueDefaultGenerator(0.5),#FIXME Default
                   "crop_factor": SimpleValueDefaultGenerator(0.5),#FIXME Default
                   #CO2 model defaults
                   "part_of_urea_in_UAN": SimpleValueDefaultGenerator(0.5),
                   "magnesium_from_fertilizer": SimpleValueDefaultGenerator(0.0),
                   "magnesium_as_dolomite": SimpleValueDefaultGenerator(1.0),
                   #Irrigation defaults
                   #NO3 model defaults
                   "bulk_density_of_soil": SimpleValueDefaultGenerator(1300.0),
                   "c_per_n_ratio": SimpleValueDefaultGenerator(11.0),
                   "clay_content": TableLookupDefaultGenerator("country", CLAY_CONTENT_PER_COUNTRY),
                   "considered_soil_volume": SimpleValueDefaultGenerator(5000.0),
                   "drained_part": SimpleValueDefaultGenerator(0.0),
                   "fertilisers_gas_losses": SimpleValueDefaultGenerator(0.1),
                   "organic_carbon_content": TableLookupDefaultGenerator("country", CARBON_CONTENT_PER_COUNTRY),
                   "nitrogen_uptake_by_crop": SimpleValueDefaultGenerator(0.0), #FIXME: Default
                   "norg_per_ntotal_ratio": SimpleValueDefaultGenerator(0.85),
                   "rooting_depth": TableLookupDefaultGenerator("crop", ROOTING_DEPTH_PER_CROP), #FIXME: Missing some default
                   #N2Ox defaults
                   "nitrogen_from_crop_residues": SimpleValueDefaultGenerator(0.0), #FIXME: Default
                   #P defaults
                   "eroded_reaching_river": SimpleValueDefaultGenerator(0.2),
                   "eroded_soil_p_enrichment": SimpleValueDefaultGenerator(1.86),
                   "p_content_in_soil": SimpleValueDefaultGenerator(0.00095),
                   "land_use_category": SimpleValueDefaultGenerator(LandUseCategory.arable_land), #FIXME: Default
                   #HM defaults
                   "hm_land_use_category": LandUseCategoryForHMDefaultGenerator(),
                   #"pesticides_quantities": TODO
                   }
