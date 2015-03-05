from enum import Enum

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
    
#FIXME: Should this be elsewhere?
_WORLD_PROPORTIONS_OF_LIQUID_MANURE = {LiquidManureType.cattle: 0.6608,
                                       LiquidManureType.fattening_pigs: 0.2179,
                                       LiquidManureType.laying_hens: 0.0870,
                                       LiquidManureType.sows_and_piglets: 0.0343,
                                      }

_WORLD_PROPORTIONS_OF_SOLID_MANURE = {SolidManureType.broiler_litter: 0.1029,
                                      SolidManureType.cattle: 0.5505,
                                      SolidManureType.horses: 0.0038,
                                      SolidManureType.laying_hen_litter: 0.0724,
                                      SolidManureType.pigs: 0.2101,
                                      SolidManureType.sheep_goats: 0.0603,
                                     }

class ManureModel(object):
    """Inputs:
      liquid_manure_part_before_dilution: ratio
      liquid_manure_proportions: map LiquidManureType -> ratio. Sum should be 1
      solid_manure_proportions: map SolidManureType -> ratio. Sum should be 1
      total_liquid_manure: m3/ha
      total_solid_maure: t/ha
      
    #FIXME: Should the outputs be also in input, in case of known value?
    Outputs:
      computeP2O5: tuple of:
        P2O5 contents liquid manure: kg P2O5/ha
        P2O5 contents solid manure: kg P2O5/ha
      computeN: tuple of:
        N total liquid manure: kg N/ha
        N total solid manure: kg N/ha
      computeNH4N: tuple of:
        NH4-N total liquid manure: kg NH4-N/ha
        NH4-N total solid manure: kg NH4-N/ha
    """
    
    _input_variables = ["liquid_manure_part_before_dilution",
                        "liquid_manure_proportions",
                        "solid_manure_proportions",
                        "total_liquid_manure",
                        "total_solid_maure"
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
    
    _NH4N_CONCENTRATION_IN_LIQUID_MANURE = {LiquidManureType.cattle: 2.75*0.55,
                                            LiquidManureType.fattening_pigs: 4.2*0.4,
                                            LiquidManureType.laying_hens: 6.3*0.69,
                                            LiquidManureType.sows_and_piglets: 3.3*0.29,
                                            LiquidManureType.other: 1.7765461 #Sumprod of concentration with world proportions
                                           }
    
    _NH4N_CONCENTRATION_IN_SOLID_MANURE = {SolidManureType.broiler_litter: 10.0*0.66,
                                           SolidManureType.cattle: 1.05*0.79,
                                           SolidManureType.horses: 0.7*0.9,
                                           SolidManureType.laying_hen_litter: 7*0.69,
                                           SolidManureType.pigs: 2.3*0.81,
                                           SolidManureType.sheep_goats: 2.3*0.9,
                                           SolidManureType.other: 2.00410305 #Sumprod of concentration with world proportions
                                          }
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in ManureModel._input_variables:
            setattr(self, key, inputs[key])
            
    def computeP2O5(self):
        return (self._sum_prod(self._P205_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_proportions) * self.total_liquid_manure
                    * self.liquid_manure_part_before_dilution,
                self._sum_prod(self._P205_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_proportions) * self.total_solid_maure)
        
    def computeN(self):
        return (self._sum_prod(self._N_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_proportions) * self.total_liquid_manure
                    * self.liquid_manure_part_before_dilution,
                self._sum_prod(self._N_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_proportions) * self.total_solid_maure)
        
    def computeNH4N(self):
        return (self._sum_prod(self._NH4N_CONCENTRATION_IN_LIQUID_MANURE, self.liquid_manure_proportions) * self.total_liquid_manure
                    * self.liquid_manure_part_before_dilution,
                self._sum_prod(self._NH4N_CONCENTRATION_IN_SOLID_MANURE, self.solid_manure_proportions) * self.total_solid_maure)
        
    def _sum_prod(self, reference, factors):
        return sum(v*factors[k] for k,v in reference.items())
        
