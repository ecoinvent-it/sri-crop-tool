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
                    "seeds":50.0
                    }
        expectedResults = {"m_transport_transport_tkm":
                           {
                            TransportDataset.transport_lorry_sup_16t_fleet_average_RER:399.5,
                            TransportDataset.transport_transoceanic_freight_ship_OCE:6155.0,
                            TransportDataset.transport_freight_rail_RER:615.0,
                            #TransportDataset.transport_freight_rail_diesel_US:0.0, #nothing as country is not US or CAN
                            }
                           }
        
        results = transportmodel.TransportModel(inputs).compute()
        
        for key, value in expectedResults.items():
            for k,v in value.items():
                self.assertAlmostEqual(results[key][k], v)
        
if __name__ == "__main__":
    unittest.main()
