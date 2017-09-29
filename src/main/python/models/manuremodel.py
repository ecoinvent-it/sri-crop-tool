from enum import Enum

from models.atomicmass import N_TO_NH3_FACTOR
from models.modelEnums import HeavyMetalType


class LiquidManureType(Enum):
    cattle = "liquid_manure_cattle"
    pig = "liquid_manure_pig"
    laying_hens = "liquid_manure_laying_hen"
    other = "liquid_manure_other"

class SolidManureType(Enum):
    cattle = "solid_manure_cattle"
    horses = "solid_manure_horses"
    laying_hen_litter = "solid_manure_laying_hen"
    pigs = "solid_manure_pig"
    sheep_goats = "solid_manure_sheep_goat"
    other = "solid_manure_other"


class ManureTypeForHM(Enum):
    cattle_liquid=1,
    cattle_low_excrement=2,#liquid
    cattle_stackable=3,#solid
    cattle_loose_housing=4,#solid
    pig_liquid=5,
    pig_solid=6,
    laying_hen_manure=7,#liquid
    laying_hen_litter=8#solid #( + broiler)

class ManureModel(object):
    """Inputs:
      liquid_manure_part_before_dilution: ratio
      liquid_manure_quantities: map LiquidManureType -> m3/ha
      solid_manure_quantities: map SolidManureType -> kg/ha

    Outputs:
      computeP2O5: tuple of:
        P2O5 contents liquid manure: kg P2O5/ha
        P2O5 contents solid manure: kg P2O5/ha
      computeN:
        N_total_manure: kg N/ha
      computeNH3:
        nh3_total_manure: kg NH3/ha
      computeHeavyMetal:
        hm_total_manure : map HeavyMetalType -> mg i/ha (i:hm type)
    """

    _input_variables = ["liquid_manure_part_before_dilution",
                        "liquid_manure_quantities",
                        "solid_manure_quantities"
                       ]

    _P205_CONCENTRATION_IN_LIQUID_MANURE = {LiquidManureType.cattle: 1.5,
                                            LiquidManureType.pig: 3.5,#mean fattening pigs + piglets
                                            LiquidManureType.laying_hens: 17.0,
                                            LiquidManureType.other: 3.40798 #Sumprod of concentration with world proportions
                                           }

    _P205_CONCENTRATION_IN_SOLID_MANURE = {SolidManureType.cattle: 2.7,
                                           SolidManureType.horses: 5.0,
                                           SolidManureType.laying_hen_litter: 25.0,#mean with broiler
                                           SolidManureType.pigs: 7.0,
                                           SolidManureType.sheep_goats: 3.3,
                                           SolidManureType.other: 7.40504 #Sumprod of concentration with world proportions
                                          }

    _N_CONCENTRATION_IN_LIQUID_MANURE = {LiquidManureType.cattle: 4.6,
                                         LiquidManureType.pig: 5.35,#mean fattening pigs + piglets
                                         LiquidManureType.laying_hens: 21.0,
                                         LiquidManureType.other: 6.33529 #Sumprod of concentration with world proportions
                                        }

    _N_CONCENTRATION_IN_SOLID_MANURE = {SolidManureType.cattle: 5.1,
                                        SolidManureType.horses: 6.8,
                                        SolidManureType.laying_hen_litter: 30.5,#mean with broiler
                                        SolidManureType.pigs: 7.8,
                                        SolidManureType.sheep_goats: 8.0,
                                        SolidManureType.other: 10.40797 #Sumprod of concentration with world proportions
                                       }
    _NH3N_CONCENTRATION_IN_LIQUID_MANURE = {LiquidManureType.cattle: 2.75*0.55,
                                            LiquidManureType.pig: 3.75*0.345,# mean fattening pigs + piglets
                                            LiquidManureType.laying_hens: 6.3*0.69,
                                            LiquidManureType.other: 1.7765461 #Sumprod of concentration with world proportions
                                           }

    _NH3N_CONCENTRATION_IN_SOLID_MANURE = {SolidManureType.cattle: 1.05*0.79,
                                           SolidManureType.horses: 0.7*0.9,
                                           SolidManureType.laying_hen_litter: 8.5*0.675,#mean with broiler
                                           SolidManureType.pigs: 2.3*0.81,
                                           SolidManureType.sheep_goats: 2.3*0.9,
                                           SolidManureType.other: 2.00410305 #Sumprod of concentration with world proportions
                                          }

    _LIQUID_MANURE_TO_HM_MANURE = { LiquidManureType.cattle: {
                                        ManureTypeForHM.cattle_liquid: 0.5,
                                        ManureTypeForHM.cattle_low_excrement: 0.5
                                        },
                                    LiquidManureType.pig:{
                                        ManureTypeForHM.pig_liquid: 1.0
                                        },
                                    LiquidManureType.laying_hens:{
                                        ManureTypeForHM.laying_hen_manure: 1.0
                                        },
                                    LiquidManureType.other:{
                                        ManureTypeForHM.cattle_liquid: 0.33,
                                        ManureTypeForHM.cattle_low_excrement: 0.33,
                                        ManureTypeForHM.pig_liquid: 0.25,
                                        ManureTypeForHM.laying_hen_manure: 0.09
                                        }
                                   }

    _SOLID_MANURE_TO_HM_MANURE = {  SolidManureType.cattle:{
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
                                        ManureTypeForHM.laying_hen_litter: 0.17 #(+ broiler)
                                    }
                                }

    _HM_MANURE_DM = {   ManureTypeForHM.cattle_liquid: 0.090,
                        ManureTypeForHM.cattle_low_excrement: 0.075,
                        ManureTypeForHM.cattle_stackable: 0.190,
                        ManureTypeForHM.cattle_loose_housing: 0.210,
                        ManureTypeForHM.pig_liquid: 0.050,
                        ManureTypeForHM.pig_solid: 0.270,
                        ManureTypeForHM.laying_hen_manure: 0.300,
                        ManureTypeForHM.laying_hen_litter: 0.550 # mean with broiler
                    }

    #mg/kg DM
    _HM_MANURE_VALUES = {
                             ManureTypeForHM.cattle_liquid: [0.18, 37.1, 162.2, 3.77, 4.3, 3.9, 0.4],
                             ManureTypeForHM.cattle_low_excrement: [0.16, 19.1, 123.3, 2.92, 3.1, 2.1, 0.6],
                             ManureTypeForHM.cattle_stackable: [ 0.17, 23.9, 117.7, 3.77, 4.3, 3.9, 0.4],
                             ManureTypeForHM.cattle_loose_housing: [ 0.15, 22.0, 91.1, 2.81, 4.3, 3.9, 0.4],
                             ManureTypeForHM.pig_liquid: [0.21, 115.3, 746.5, 1.76, 8.6, 6.7, 0.8],
                             ManureTypeForHM.pig_solid: [0.21, 115.3, 746.5, 1.76, 8.6, 6.7, 0.8],
                             ManureTypeForHM.laying_hen_manure: [0.25, 39.6, 468.4, 2.24, 7.9, 5.5, 0.2],
                             ManureTypeForHM.laying_hen_litter: [0.27, 41.7, 408.8, 2.58, 23.95, 7.75, 0.2] # mean with broiler
                             }

    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in ManureModel._input_variables:
            setattr(self, key, inputs[key])

    def computeP2O5(self):
        return (self._sum_prod(self._P205_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_quantities)
                    * self.liquid_manure_part_before_dilution,
                self._sum_prod(self._P205_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_quantities)/ 1000.0)

    def computeN(self):
        return self._sum_prod(self._N_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_quantities) \
                    * self.liquid_manure_part_before_dilution \
                + self._sum_prod(self._N_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_quantities)/ 1000.0

    def computeNH3(self):
        nh3_as_n_liquid = self._sum_prod(self._NH3N_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_quantities) * self.liquid_manure_part_before_dilution
        nh3_as_n_solid = self._sum_prod(self._NH3N_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_quantities)/ 1000.0
        return nh3_as_n_liquid * N_TO_NH3_FACTOR \
               + nh3_as_n_solid * N_TO_NH3_FACTOR

    def _sum_prod(self, reference, factors):
        return sum(v*factors[k] for k,v in reference.items())

    def computeHeavyMetal(self):
        hm_manure_total_values = dict.fromkeys(self._HM_MANURE_VALUES.keys(),0.0)
        self._convert_manure_input_types_to_manure_for_HM(hm_manure_total_values,
                                                          self.solid_manure_quantities,
                                                          self._SOLID_MANURE_TO_HM_MANURE,
                                                          factor=0.001)
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
