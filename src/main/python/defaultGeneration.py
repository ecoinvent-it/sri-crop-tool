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

DEFAULTS_VALUES_GENERATORS = {
                   #Cross-models defaults
                   #Fertiliser defaults
                   "soil_with_ph_under_or_7": SimpleValueDefaultGenerator(0.5), #FIXME: Default
                   #Manure defaults
                   "liquid_manure_part_before_dilution": SimpleValueDefaultGenerator(0.5),
                   #CO2 model defaults
                   "part_of_urea_in_UAN": SimpleValueDefaultGenerator(0.5),
                   "magnesium_from_fertilizer": SimpleValueDefaultGenerator(0.0),
                   "magnesium_as_dolomite": SimpleValueDefaultGenerator(1.0),
                   #Irrigation defaults
                   #NO3 model defaults
                   "bulk_density_of_soil": SimpleValueDefaultGenerator(1300.0),
                   "c_per_n_ratio": SimpleValueDefaultGenerator(11.0),
                   "clay_content": "TODO_DEFAULT_FROM_TABLE",
                   "considered_soil_volume": SimpleValueDefaultGenerator(5000.0),
                   "drained_part": SimpleValueDefaultGenerator(0.0),
                   "fertilisers_gas_losses": SimpleValueDefaultGenerator(0.1),
                   "organic_carbon_content": "TODO_DEFAULT_FROM_TABLE",
                   "nitrogen_uptake_by_crop": "TODO",
                   "norg_per_ntotal_ratio": SimpleValueDefaultGenerator(0.85),
                   "precipitation": "TODO",
                   "rooting_depth": "TODO_DEFAULT_FROM_TABLE",
                   #N2Ox defaults
                   "nitrogen_from_crop_residues": SimpleValueDefaultGenerator(0.0) #FIXME: Default
                   }
