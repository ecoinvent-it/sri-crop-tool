import unittest
import models.transportmodel as transportmodel
from models.transportmodel import TransportDataset

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"country":"CH",
                    "total_composttype":100.0,
                    "total_sewagesludge":200.0,
                    "nitrogen_from_mineral_fert":150.0,
                    "p2O5_from_mineral_fert":200.0,
                    "k2O_from_mineral_fert":250.0,
                    "magnesium_from_fertilizer":5.0,
                    "ca_from_mineral_fert":10.0,
                    "pest_total":500.0,
                    "pest_horticultural_oil":2.6,
                    "seeds":50.0
                    }
        expectedResults = {
                            TransportDataset.wfldb_transport_lorry_sup_16t:400.8,
                            TransportDataset.wfldb_transport_transoceanic:6168.0,
                            TransportDataset.wfldb_transport_freight_rail_RER:615.0,
                            TransportDataset.ecoinvent_transport_van_inf_3_5t:2.373,
                            TransportDataset.ecoinvent_transport_lorry_sup_16t:126.6,
                            TransportDataset.ecoinvent_transport_barge: 480.0,
                            TransportDataset.ecoinvent_transport_freight_rail:123.6
                           }
        
        results = transportmodel.TransportModel(inputs).compute()
        
        for k, v in expectedResults.items():
            self.assertAlmostEqual(results[k], v)
                
    def testModelForCA(self):
        inputs = {"country":"CA",
                    "total_composttype":100.0,
                    "total_sewagesludge":200.0,
                    "nitrogen_from_mineral_fert":150.0,
                    "p2O5_from_mineral_fert":200.0,
                    "k2O_from_mineral_fert":250.0,
                    "magnesium_from_fertilizer":5.0,
                    "ca_from_mineral_fert":10.0,
                    "pest_total":500.0,
                    "pest_horticultural_oil":2.6,
                    "seeds":50.0
                    }
        expectedResults = {
                            TransportDataset.wfldb_transport_lorry_sup_16t:400.8,
                            TransportDataset.wfldb_transport_transoceanic:6168.0,
                            TransportDataset.wfldb_transport_freight_rail_diesel_US:615.0,
                            TransportDataset.ecoinvent_transport_van_inf_3_5t:2.373,
                            TransportDataset.ecoinvent_transport_lorry_sup_16t:126.6,
                            TransportDataset.ecoinvent_transport_barge: 480.0,
                            TransportDataset.ecoinvent_transport_freight_rail:123.6
                            }
        
        results = transportmodel.TransportModel(inputs).compute()
        
        for k, v in expectedResults.items():
            self.assertAlmostEqual(results[k], v)
        
if __name__ == "__main__":
    unittest.main()
