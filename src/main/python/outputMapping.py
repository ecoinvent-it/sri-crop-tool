
class OutputMapping(object):
    
    def __init__(self, yield_main_product_per_crop_cycle):
        #self.allInputs = allInputs
        self.yield_main_product_per_crop_cycle = yield_main_product_per_crop_cycle
        self.output = {}
        
    def mapCo2Model(self, co2Output):
        for key, value in co2Output.items():
            self.output[key.replace("m_co2_", "")] = value / self.yield_main_product_per_crop_cycle
