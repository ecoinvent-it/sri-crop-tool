from enum import Enum
from models.modelEnums import HeavyMetalType
      
class PesticideType(Enum):
        cu=1
        mancozeb=2
        metiram=3
        propineb=4
        zineb=5
        ziram=6
        
class LandUseCategoryForHM(Enum):
        permanent_grassland=1
        arable_land=2
        horticultural_crops=3

class HmModel(object):
    
    """Inputs:
      hm_values_for_manure : map HeavyMetalType -> mg i/(ha*year) (i:hm type)
      hm_values_for_mineral_fertilisers : map HeavyMetalType -> mg i/(ha*year) (i:hm type)
      hm_values_for_other_fertilisers : map HeavyMetalType -> mg i/(ha*year) (i:hm type)
      hm_values_for_seeds: map HeavyMetalType -> mg i/(ha*year) (i:hm type)
      pesticides_quantities: map PesticideType -> kg i /(ha*year) (i: pest type)
      drainage: ratio
      eroded_soil: kg/(ha*year)
      land_use_category: LandUseCategoryForHM
      
    Outputs:
        m_hm_to_soil: map HeavyMetalType -> kg i/(ha*year) (i:hm type)
        m_hm_to_ground_water: map HeavyMetalType -> kg i/(ha*year) (i:hm type)
        m_hm_to_surface_water: map HeavyMetalType -> kg i/(ha*year) (i:hm type)
        
    elements:    
        Cd: Cadmium
        Cu: Copper
        Zn: Zinc
        Pb: Lead
        Ni: Nickel
        Cr: Chromium
        Hg: Mercury

    """
    
    _input_variables = ['hm_values_for_manure',
                        'hm_values_for_mineral_fertilisers',
                        'hm_values_for_other_fertilisers',
                        'hm_values_for_seeds',
                        'pesticides_quantities',
                        'drainage',
                        'eroded_soil',
                        'land_use_category'
                       ]
    
    #mg/ha/y
    _HM_DEPOSITIONS = {
                        HeavyMetalType.cd: 700.0,
                        HeavyMetalType.cu: 2400.0,
                        HeavyMetalType.zn: 90400.0,
                        HeavyMetalType.pb: 18700.0,
                        HeavyMetalType.ni: 5475.0,
                        HeavyMetalType.cr: 3650.0,
                        HeavyMetalType.hg: 50.0
                        }
    
    _PEST_TO_SOIL_RATIO = 0.95
    _ZINC_MW = 65.39
    
    _PEST_NB_ZN_ATOMS = {
                         PesticideType.propineb: 1,
                         PesticideType.mancozeb: 1,
                         PesticideType.metiram: 3,
                         PesticideType.zineb: 1,
                         PesticideType.ziram: 1
                        }
    
    _PEST_MW = {
                    PesticideType.propineb: 289.79,     #C5H8N2S4Zn
                    PesticideType.mancozeb: 541.07,     #(C4H6MnN2S4)x·(C4H6N2S4Zn)y with x:y=94:6
                    PesticideType.metiram: 1088.70,     #C16H33N11S16Zn3
                    PesticideType.zineb: 275.76,        #C4H6N2S4Zn
                    PesticideType.ziram: 305.83         #C6H12N2S4Zn
                    }
    
    #mg/ha/y
    _LEACHING_TO_GW= {
                        HeavyMetalType.cd: 50.0,
                        HeavyMetalType.cu: 3600.0,
                        HeavyMetalType.zn: 33000.0,
                        HeavyMetalType.pb: 600.0,
                        HeavyMetalType.ni: 0.0, #NA
                        HeavyMetalType.cr: 21200.0,
                        HeavyMetalType.hg: 1.3
                        }
    
    _ACCUMULATION_FACTOR = 0.86
    _EROSION_FACTOR = 0.2
    
    #mg/kg
    _SOIL_HM_CONTENT = {LandUseCategoryForHM.permanent_grassland: [0.309, 18.3, 64.6, 24.6, 22.3, 24.0, 0.088],
                        LandUseCategoryForHM.arable_land: [0.24, 20.1, 49.6, 19.5, 23.0, 24.1, 0.073],
                        LandUseCategoryForHM.horticultural_crops: [0.307, 39.2, 70.1, 24.9, 24.8, 27.0, 0.077]
                        }
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in HmModel._input_variables:
            setattr(self, key, inputs[key])
            
    def compute(self):
        sumPest = self._compute_pesticides()
        hm_to_gw = dict.fromkeys(HeavyMetalType,0.0)
        hm_to_sw = dict.fromkeys(HeavyMetalType,0.0)
        hm_to_soil = dict.fromkeys(HeavyMetalType,0.0)
        
        for hmIndex, hmElement in enumerate(HeavyMetalType):
            agro_input = self._compute_agro_input(sumPest, hmElement);
            allocation_factor = self._compute_allocation_factor_for_hm_element(agro_input,hmElement);
            leaching = self._compute_leaching_for_hm_element(allocation_factor,hmElement);
            erosion_gw = self._compute_erosion_gw(allocation_factor, hmElement, hmIndex);
            hm_to_soil[hmElement] = self._compute_soil(agro_input, allocation_factor, leaching, erosion_gw, hmElement)
            hm_to_sw[hmElement], hm_to_gw[hmElement] = self._split_leaching_between_surface_and_ground_water(leaching);
            hm_to_gw[hmElement] += erosion_gw;
            
        return {'m_hm_to_soil':hm_to_soil,
                'm_hm_to_ground_water':hm_to_gw,
                'm_hm_to_surface_water':hm_to_sw}
        
    def _compute_agro_input(self, sumPest, hmElement):
        return self.hm_values_for_manure[hmElement] \
                + self.hm_values_for_mineral_fertilisers[hmElement]\
                + self.hm_values_for_other_fertilisers[hmElement]\
                + sumPest[hmElement] \
                + self.hm_values_for_seeds[hmElement];
    
    def _compute_allocation_factor_for_hm_element(self, agro_input, hmElement):
        return agro_input / (agro_input + self._HM_DEPOSITIONS[hmElement])
        
    def _compute_pesticides(self):
        hm_values = dict.fromkeys(HeavyMetalType,0.0)
        hm_values[HeavyMetalType.cu] = self.pesticides_quantities[PesticideType.cu] * self._PEST_TO_SOIL_RATIO
        for pest in self._PEST_NB_ZN_ATOMS.keys():
            hm_values[HeavyMetalType.zn] += self._compute_pesticides_ratio_to_zn(pest) \
                                            * self.pesticides_quantities[pest] \
                                            * self._PEST_TO_SOIL_RATIO
        return hm_values
            
    def _compute_pesticides_ratio_to_zn(self,pest):#mg/g -> mg/kg
        return self._ZINC_MW * self._PEST_NB_ZN_ATOMS[pest] / self._PEST_MW[pest] / 1000.0
    
    def _compute_leaching_for_hm_element(self, allocation_factor, hmElement): # mg/ha/y > kg/ha/y
        return self._LEACHING_TO_GW[hmElement] * allocation_factor / 1000000.0;
  
    def _split_leaching_between_surface_and_ground_water(self, total_leaching):
        surface = total_leaching * self.drainage
        ground = total_leaching  * (1.0 - self.drainage)
        return (surface,ground)
    
    def _compute_erosion_gw(self, allocation_factor, hmElement, hmIndex): #mg/ha -> kg/ha
        return self._SOIL_HM_CONTENT[self.land_use_category][hmIndex] / 1000000.0 \
                * self.eroded_soil * self._ACCUMULATION_FACTOR \
                * self._EROSION_FACTOR * allocation_factor

    def _compute_soil(self, agro_input, allocation_factor, leaching, erosion_gw, hmElement):
        return (agro_input \
               -(leaching + erosion_gw)/allocation_factor ) \
                * allocation_factor 
