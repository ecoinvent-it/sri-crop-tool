import unittest
from models.hmmodel import HeavyMetalType
from models.seedmodel import SeedModel

class Test(unittest.TestCase):
    inputs = {"seed_quantities":
                  { "almond": 4.27, #tree_seedlings
                "apple": 4.27, #tree_seedlings
                "apricot": 4.27, #tree_seedlings
                "asparagus": 3.7875, #vegetable
                "banana": 4.27, #tree_seedlings
                "carrot": 3.7875, #vegetable
                "cocoa": 3.91375, #other
                "coconut": 4.27, #tree_seedlings
                "coffee": 3.91375, #other
                "lemonlime": 4.27, #tree_seedlings
                "linseed": 11.11, #pea
                "maizegrain": 0.85,
                "mandarin": 4.27, #tree_seedlings
                "mint": 3.7875, #vegetable
                "oat": 3.91375, #other
                "olive": 4.27, #tree_seedlings
                "onion": 3.7875, #vegetable
                "orange": 4.27, #tree_seedlings
                "palmtree": 4.27, #tree_seedlings
                "peach": 4.27, #tree_seedlings
                "peanut": 3.91375, #other
                "pear": 4.27, #tree_seedlings
                "pineapple": 4.31, #tree_seedlings
                "potato": 27.27,
                "rapeseed": 17.17,
                "rice":24.745, #wheat
                "soybean": 40.41,
                "strawberry": 3.91375, #other
                "sugarbeet": 26.26,
                "sugarcane": 3.91375, #other
                "sunflower": 45.45,
                "sweetcorn": 31.31, #maize
                "tea": 28.65875, #other
                "tomato": 3.91375, #other
                "wheat": 115.445
                   
                  }
             }
    
    def testHeavyMetal(self):
        expectedResults = {
                            HeavyMetalType.cd: 62.531239,
                            HeavyMetalType.cu: 2285.08547,
                            HeavyMetalType.zn: 13529.576330,
                            HeavyMetalType.pb: 196.58079099,
                            HeavyMetalType.ni: 444.903968999,
                            HeavyMetalType.cr: 159.0201995,
                            HeavyMetalType.hg: 8.51967}
                        
        results = SeedModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
                self.assertAlmostEqual(results[key], value)

    
if __name__ == "__main__":
    unittest.main()
