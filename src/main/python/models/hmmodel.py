from enum import Enum

from models.modelEnums import HeavyMetalType


class PesticideType(Enum):
        cu="copper_cu" #fungicide
        mancozeb="mancozeb" #fungicide
        metiram="metiram" #fungicide
        propineb="propineb" #fungicide
        zineb="zineb" #fungicide
        ziram="ziram" #fungicide

class LandUseCategoryForHM(Enum):
        permanent_grassland=1
        arable_land=2
        horticultural_crops=3

class HmModel(object):
    """Inputs:
      crop_cycle_per_year: ratio
      hm_from_manure : map HeavyMetalType -> mg i/(ha*crop cycle) (i:hm type)
      hm_from_mineral_fert : map HeavyMetalType -> mg i/(ha*crop cycle) (i:hm type)
      hm_from_other_organic_fert : map HeavyMetalType -> mg i/(ha*crop cycle) (i:hm type)
      hm_from_seed: map HeavyMetalType -> mg i/(ha*crop cycle) (i:hm type)
      hm_pesticides_quantities: map PesticideType -> g i /(ha*crop cycle) (i: pest type)
      drained_part: ratio
      eroded_soil: kg/(ha*year)
      hm_land_use_category: LandUseCategoryForHM
      yield_main_product_dry_per_crop_cycle: kg

    Outputs:
        m_hm_heavymetal_to_soil: map HeavyMetalType -> kg i/(ha*crop cycle) (i:hm type)
        m_hm_heavymetal_to_ground_water: map HeavyMetalType -> kg i/(ha*crop cycle) (i:hm type)
        m_hm_heavymetal_to_surface_water: map HeavyMetalType -> kg i/(ha*crop cycle) (i:hm type)

    elements:
        Cd: Cadmium
        Cu: Copper
        Zn: Zinc
        Pb: Lead
        Ni: Nickel
        Cr: Chromium
        Hg: Mercury

    """

    _input_variables = ['crop_cycle_per_year',
                        'hm_from_manure',
                        'hm_from_mineral_fert',
                        'hm_from_other_organic_fert',
                        'hm_from_seed',
                        'hm_pesticides_quantities',
                        'drained_part',
                        'eroded_soil',
                        'hm_land_use_category',
                        'yield_main_product_dry_per_crop_cycle'
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
                        HeavyMetalType.ni: 0.0, #NA NOTE: no value in src
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
        sumPest = self._compute_pesticides() #mg/ha
        hm_to_gw = dict.fromkeys(HeavyMetalType,0.0)
        hm_to_sw = dict.fromkeys(HeavyMetalType,0.0)
        hm_to_soil = dict.fromkeys(HeavyMetalType,0.0)

        for hmIndex, hmElement in enumerate(HeavyMetalType):
            agro_input = self._compute_agro_input(sumPest, hmElement); #mg/ha
            allocation_factor = self._compute_allocation_factor_for_hm_element(agro_input,hmElement); #ratio
            leaching = self._compute_leaching_for_hm_element(allocation_factor,hmElement); #mg/ha
            erosion_sw = self._compute_erosion_sw(allocation_factor, hmElement, hmIndex); #mg/ha
            hm_to_soil[hmElement] = self._compute_soil(agro_input, allocation_factor, leaching, erosion_sw, hmElement)
            hm_to_sw[hmElement], hm_to_gw[hmElement] = self._split_leaching_between_surface_and_ground_water(leaching)
            hm_to_sw[hmElement] += erosion_sw
            hm_to_soil[hmElement] /= 1000000.0
            hm_to_sw[hmElement] /= 1000000.0
            hm_to_gw[hmElement] /= 1000000.0

        return {'m_hm_heavymetal_to_soil':hm_to_soil,
                'm_hm_heavymetal_to_ground_water':hm_to_gw,
                'm_hm_heavymetal_to_surface_water': hm_to_sw,
                'm_hm_heavymetal_to_soil_minus_uptake':
                    {
                        HeavyMetalType.cd:
                            hm_to_soil[HeavyMetalType.cd] - self.yield_main_product_dry_per_crop_cycle * 1.0154e-7,
                        HeavyMetalType.cu:
                            hm_to_soil[HeavyMetalType.cu] - self.yield_main_product_dry_per_crop_cycle * 6.6269e-6,
                        HeavyMetalType.zn:
                            hm_to_soil[HeavyMetalType.zn] - self.yield_main_product_dry_per_crop_cycle * 3.2023e-5,
                        HeavyMetalType.pb:
                            hm_to_soil[HeavyMetalType.pb] - self.yield_main_product_dry_per_crop_cycle * 5.4077e-7,
                        HeavyMetalType.ni:
                            hm_to_soil[HeavyMetalType.ni] - self.yield_main_product_dry_per_crop_cycle * 1.0446e-6,
                        HeavyMetalType.cr:
                            hm_to_soil[HeavyMetalType.cr] - self.yield_main_product_dry_per_crop_cycle * 5.45e-7,
                        HeavyMetalType.hg:
                            hm_to_soil[HeavyMetalType.hg] - self.yield_main_product_dry_per_crop_cycle * 4.0556e-8
                    }}

    def _compute_agro_input(self, sumPest, hmElement):
        return    self.hm_from_manure[hmElement] \
                + self.hm_from_mineral_fert[hmElement]\
                + self.hm_from_other_organic_fert[hmElement]\
                + self.hm_from_seed[hmElement] \
                + sumPest[hmElement]

    def _compute_allocation_factor_for_hm_element(self, agro_input, hmElement):
        return agro_input / (agro_input + self._HM_DEPOSITIONS[hmElement] / self.crop_cycle_per_year)

    def _compute_pesticides(self): #mg/ha
        hm_values = dict.fromkeys(HeavyMetalType,0.0)
        #g/ha -> mg/ha
        hm_values[HeavyMetalType.cu] = self.hm_pesticides_quantities[PesticideType.cu] * self._PEST_TO_SOIL_RATIO * 1000.0
        for pest in self._PEST_NB_ZN_ATOMS.keys():
            hm_values[HeavyMetalType.zn] += self._compute_pesticides_ratio_to_zn(pest) \
                                            * self.hm_pesticides_quantities[pest] \
                                            * self._PEST_TO_SOIL_RATIO * 1000.0
        return hm_values

    def _compute_pesticides_ratio_to_zn(self,pest): #ratio
        return self._ZINC_MW / self._PEST_MW[pest] * self._PEST_NB_ZN_ATOMS[pest]

    def _compute_leaching_for_hm_element(self, allocation_factor, hmElement): # mg/ha/y > mg/ha/crop cycle
        return self._LEACHING_TO_GW[hmElement] / self.crop_cycle_per_year * allocation_factor;

    def _split_leaching_between_surface_and_ground_water(self, total_leaching): #mg/ha
        surface = total_leaching * self.drained_part
        ground = total_leaching  * (1.0 - self.drained_part)
        return (surface,ground)

    def _compute_erosion_sw(self, allocation_factor, hmElement, hmIndex): #mg/ha
        return self._SOIL_HM_CONTENT[self.hm_land_use_category][hmIndex] \
                * self.eroded_soil / self.crop_cycle_per_year * self._ACCUMULATION_FACTOR \
                * self._EROSION_FACTOR * allocation_factor

    def _compute_soil(self, agro_input, allocation_factor, leaching, erosion_gw, hmElement): #mg/ha
        return 0.0 if allocation_factor == 0.0 else (agro_input \
               -(leaching + erosion_gw)/allocation_factor ) \
                * allocation_factor
