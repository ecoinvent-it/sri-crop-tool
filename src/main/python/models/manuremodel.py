from enum import Enum
from models.modelEnums import HeavyMetalType
from models.atomicmass import N_TO_NH3_FACTOR

class LiquidManureType(Enum):
    cattle=1
    fattening_pigs=2
    laying_hens=3
    sows_and_piglets=4
    other=5

class SolidManureType(Enum):
    broiler_litter=1
    cattle=2
    horses=3
    laying_hen_litter=4
    pigs=5
    sheep_goats=6
    other=7
    
class ManureTypeForHM(Enum):
    cattle_liquid=1,
    cattle_low_excrement=2,#liquid
    cattle_stackable=3,#solid
    cattle_loose_housing=4,#solid
    pig_liquid=5,
    pig_solid=6,
    broiler=7,#solid
    laying_hen_manure=8,#liquid
    laying_hen_litter=9#solid

class ManureModel(object):
    """Inputs:
      liquid_manure_part_before_dilution: ratio
      liquid_manure_quantities: map LiquidManureType -> m3/ha
      solid_manure_quantities: map SolidManureType -> t/ha
      
    Outputs:
      computeP2O5: tuple of:
        P2O5 contents liquid manure: kg P2O5/ha
        P2O5 contents solid manure: kg P2O5/ha
      computeN: tuple of:
        N total liquid manure: kg N/ha
        N total solid manure: kg N/ha
      computeNH3:
        nh3_total_liquid_manure: kg NH3/ha
        nh3_total_solid_manure: kg NH3/ha
      computeHeavyMetal:
        hm_total_manure : map HeavyMetalType -> mg i/ha (i:hm type)
    """
    
    _input_variables = ["liquid_manure_part_before_dilution",
                        "liquid_manure_quantities",
                        "solid_manure_quantities"
                       ]
    
    _P205_CONCENTRATION_IN_LIQUID_MANURE = {LiquidManureType.cattle: 1.5,
                                            LiquidManureType.fattening_pigs: 3.8,
                                            LiquidManureType.laying_hens: 17.0,
                                            LiquidManureType.sows_and_piglets: 3.2,
                                            LiquidManureType.other: 3.40798 #Sumprod of concentration with world proportions
                                           }
    
    _P205_CONCENTRATION_IN_SOLID_MANURE = {SolidManureType.broiler_litter: 20.0,
                                           SolidManureType.cattle: 2.7,
                                           SolidManureType.horses: 5.0,
                                           SolidManureType.laying_hen_litter: 30.0,
                                           SolidManureType.pigs: 7.0,
                                           SolidManureType.sheep_goats: 3.3,
                                           SolidManureType.other: 7.40504 #Sumprod of concentration with world proportions
                                          }
    
    _N_CONCENTRATION_IN_LIQUID_MANURE = {LiquidManureType.cattle: 4.6,
                                         LiquidManureType.fattening_pigs: 6.0,
                                         LiquidManureType.laying_hens: 21.0,
                                         LiquidManureType.sows_and_piglets: 4.7,
                                         LiquidManureType.other: 6.33529 #Sumprod of concentration with world proportions
                                        }
    
    _N_CONCENTRATION_IN_SOLID_MANURE = {SolidManureType.broiler_litter: 34.0,
                                        SolidManureType.cattle: 5.1,
                                        SolidManureType.horses: 6.8,
                                        SolidManureType.laying_hen_litter: 27.0,
                                        SolidManureType.pigs: 7.8,
                                        SolidManureType.sheep_goats: 8.0,
                                        SolidManureType.other: 10.40797 #Sumprod of concentration with world proportions
                                       }
    _NH3N_CONCENTRATION_IN_LIQUID_MANURE = {LiquidManureType.cattle: 2.75*0.55,
                                            LiquidManureType.fattening_pigs: 4.2*0.4,
                                            LiquidManureType.laying_hens: 6.3*0.69,
                                            LiquidManureType.sows_and_piglets: 3.3*0.29,
                                            LiquidManureType.other: 1.7765461 #Sumprod of concentration with world proportions
                                           }
    
    _NH3N_CONCENTRATION_IN_SOLID_MANURE = {SolidManureType.broiler_litter: 10.0*0.66,
                                           SolidManureType.cattle: 1.05*0.79,
                                           SolidManureType.horses: 0.7*0.9,
                                           SolidManureType.laying_hen_litter: 7*0.69,
                                           SolidManureType.pigs: 2.3*0.81,
                                           SolidManureType.sheep_goats: 2.3*0.9,
                                           SolidManureType.other: 2.00410305 #Sumprod of concentration with world proportions
                                          }
    
    _LIQUID_MANURE_TO_HM_MANURE = { LiquidManureType.cattle: {
                                        ManureTypeForHM.cattle_liquid: 0.5,
                                        ManureTypeForHM.cattle_low_excrement: 0.5
                                        },
                                    LiquidManureType.fattening_pigs:{
                                        ManureTypeForHM.pig_liquid: 1.0
                                        },
                                    LiquidManureType.laying_hens:{
                                        ManureTypeForHM.laying_hen_manure: 1.0
                                        },
                                    LiquidManureType.sows_and_piglets:{
                                        ManureTypeForHM.pig_liquid: 1.0
                                        },
                                    LiquidManureType.other:{
                                        ManureTypeForHM.cattle_liquid: 0.33,
                                        ManureTypeForHM.cattle_low_excrement: 0.33,
                                        ManureTypeForHM.pig_liquid: 0.25,
                                        ManureTypeForHM.laying_hen_manure: 0.09
                                        }
                                   }
    
    _SOLID_MANURE_TO_HM_MANURE = {  SolidManureType.broiler_litter: {
                                        ManureTypeForHM.broiler: 1.0
                                    },
                                    SolidManureType.cattle:{
                                        ManureTypeForHM.cattle_stackable: 0.5,
                                        ManureTypeForHM.cattle_loose_housing: 0.5,
                                    },
                                    SolidManureType.horses:{
                                        ManureTypeForHM.cattle_loose_housing: 1.0
                                    },
                                    SolidManureType.laying_hen_litter:{
                                        ManureTypeForHM.laying_hen_litter: 1.0
                                    },
                                    SolidManureType.pigs:{
                                        ManureTypeForHM.pig_solid: 1.0
                                    },
                                    SolidManureType.sheep_goats:{
                                        ManureTypeForHM.cattle_loose_housing: 1.0
                                    },
                                    SolidManureType.other:{
                                        ManureTypeForHM.cattle_stackable: 0.31,
                                        ManureTypeForHM.cattle_loose_housing: 0.31,
                                        ManureTypeForHM.pig_solid: 0.21,
                                        ManureTypeForHM.broiler: 0.10,
                                        ManureTypeForHM.laying_hen_litter: 0.07
                                    }
                                }
    
    _HM_MANURE_DM = {   ManureTypeForHM.cattle_liquid: 0.090,
                        ManureTypeForHM.cattle_low_excrement: 0.075,
                        ManureTypeForHM.cattle_stackable: 0.190,
                        ManureTypeForHM.cattle_loose_housing: 0.210,
                        ManureTypeForHM.pig_liquid: 0.050,
                        ManureTypeForHM.pig_solid: 0.270,
                        ManureTypeForHM.broiler: 0.650,
                        ManureTypeForHM.laying_hen_manure: 0.300,
                        ManureTypeForHM.laying_hen_litter: 0.450
                    }
    
    #mg/kg DM
    _HM_MANURE_VALUES = {
                             ManureTypeForHM.cattle_liquid: [0.18, 37.1, 162.2, 3.77, 4.3, 3.9, 0.4],
                             ManureTypeForHM.cattle_low_excrement: [0.16, 19.1, 123.3, 2.92, 3.1, 2.1, 0.6],
                             ManureTypeForHM.cattle_stackable: [ 0.17, 23.9, 117.7, 3.77, 4.3, 3.9, 0.4],
                             ManureTypeForHM.cattle_loose_housing: [ 0.15, 22.0, 91.1, 2.81, 4.3, 3.9, 0.4],
                             ManureTypeForHM.pig_liquid: [0.21, 115.3, 746.5, 1.76, 8.6, 6.7, 0.8],
                             ManureTypeForHM.pig_solid: [0.21, 115.3, 746.5, 1.76, 8.6, 6.7, 0.8],
                             ManureTypeForHM.broiler: [0.29, 43.8, 349.2, 2.92, 40.0, 10.0, 0.2],
                             ManureTypeForHM.laying_hen_manure: [0.25, 39.6, 468.4, 2.24, 7.9, 5.5, 0.2],
                             ManureTypeForHM.laying_hen_litter: [0.25, 39.6, 468.4, 2.24, 7.9, 5.5, 0.2]
                             }
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in ManureModel._input_variables:
            setattr(self, key, inputs[key])
            
    def computeP2O5(self):
        return (self._sum_prod(self._P205_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_quantities)
                    * self.liquid_manure_part_before_dilution,
                self._sum_prod(self._P205_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_quantities))
        
    def computeN(self):
        return self._sum_prod(self._N_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_quantities) \
                    * self.liquid_manure_part_before_dilution \
                + self._sum_prod(self._N_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_quantities)
        
    def computeNH3(self):
        nh3_as_n_liquid = self._sum_prod(self._NH3N_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_quantities) * self.liquid_manure_part_before_dilution
        nh3_as_n_solid = self._sum_prod(self._NH3N_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_quantities)
        return nh3_as_n_liquid * N_TO_NH3_FACTOR \
               + nh3_as_n_solid * N_TO_NH3_FACTOR
        
    def _sum_prod(self, reference, factors):
        return sum(v*factors[k] for k,v in reference.items())
    
    def computeHeavyMetal(self):
        hm_manure_total_values = dict.fromkeys(self._HM_MANURE_VALUES.keys(),0.0)
        self._convert_manure_input_types_to_manure_for_HM(hm_manure_total_values,
                                                          self.solid_manure_quantities,
                                                          self._SOLID_MANURE_TO_HM_MANURE)
        self._convert_manure_input_types_to_manure_for_HM(hm_manure_total_values,
                                                          self.liquid_manure_quantities,
                                                          self._LIQUID_MANURE_TO_HM_MANURE,
                                                          factor=self.liquid_manure_part_before_dilution)
        hm_total_manure = self._compute_hm_element_values_from_hm_manure_total_values(hm_manure_total_values)
        return hm_total_manure
        
    def _convert_manure_input_types_to_manure_for_HM(self, hm_manure_total_values, manure_quantities, manureMapping, factor=None):
        if (factor is not None):
            manure_quantities_map = {k:v*factor for k,v in manure_quantities.items()}
        else:
            manure_quantities_map = manure_quantities
            
        for inputManureKey,HmManureMap in manureMapping.items():
            for hmManure,ratio in HmManureMap.items():
                hm_manure_total_values[hmManure] += ratio * manure_quantities_map[inputManureKey]   
        
    def _compute_hm_element_values_from_hm_manure_total_values(self,hm_manure_total_values):
        hm_element_values = dict.fromkeys(HeavyMetalType,0.0)

        for manureKey,manureValue in hm_manure_total_values.items():
            for hm_element_index,hm_element_value in enumerate(self._HM_MANURE_VALUES[manureKey]):
                hm_element_values[HeavyMetalType(hm_element_index)] += hm_element_value * self._HM_MANURE_DM[manureKey] * manureValue

        return hm_element_values
        
