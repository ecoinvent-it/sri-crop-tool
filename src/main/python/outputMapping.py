
class OutputMapping(object):
    
    def __init__(self):
        self.output = {}
        
    def mapCo2Model(self, co2Output):
        for key, value in co2Output.items():
            self.output[key.replace("m_co2_", "")] = value
            
    def mapNo3Model(self, no3Output):
        for key, value in no3Output.items():
            self.output[key.replace("m_No3_", "")] = value
        
