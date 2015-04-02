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
                   #CO2 model defaults
                   "nitrogen_from_urea": "TODO", #FIXME Use the same generator that will create the whole nitrogen from fert map
                   "nitrogen_from_ureaAN": "TODO", #FIXME Or use the whole map in the CO2 module
                   "part_of_urea_in_UAN": SimpleValueDefaultGenerator(0.5),
                   "calcium_from_lime": "TODO",
                   "calcium_from_carbonation_lime": "TODO",
                   "calcium_from_seaweed_lime": "TODO",
                   "magnesium_from_fertilizer": "TODO",
                   "magnesium_as_dolomite": SimpleValueDefaultGenerator(1.0),
                   #NO3 model defaults
                   "bulk_density_of_soil": SimpleValueDefaultGenerator(1300.0),
                   "c_per_n_ratio": SimpleValueDefaultGenerator(11.0),
                   "clay_content": "TODO_DEFAULT_FROM_TABLE",
                   "considered_soil_volume": SimpleValueDefaultGenerator(5000.0),
                   "drained_part": "drainage",
                   "fertilisers_gas_losses": SimpleValueDefaultGenerator(0.1),
                   "organic_carbon_content": "TODO_DEFAULT_FROM_TABLE",
                   "irrigation": "TODO",
                   "nitrogen_from_fertiliser": "TODO",
                   "nitrogen_uptake_by_crop": "TODO",
                   "norg_per_ntotal_ratio": SimpleValueDefaultGenerator(0.85),
                   "precipitation": "TODO",
                   "rooting_depth": "TODO_DEFAULT_FROM_TABLE"
                   }
