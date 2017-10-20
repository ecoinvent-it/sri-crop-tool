from enum import Enum

from models.atomicmass import N_TO_NH3_FACTOR
from models.modelEnums import HeavyMetalType


class NFertiliserType(Enum):
    ammonium_nitrate="fert_n_ammonium_nitrate"
    urea="fert_n_urea"
    ureaAN="fert_n_ureaAN"
    mono_ammonium_phosphate="fert_n_mono_ammonium_phosphate"
    di_ammonium_phosphate="fert_n_di_ammonium_phosphate"
    an_phosphate="fert_n_an_phosphate"
    lime_ammonium_nitrate="fert_n_lime_ammonium_nitrate"
    ammonium_sulphate="fert_n_ammonium_sulphate"
    potassium_nitrate="fert_n_potassium_nitrate"
    ammonia_liquid="fert_n_ammonia_liquid"

class PFertiliserType(Enum):
    triple_superphosphate="fert_p_triple_superphosphate"
    superphosphate="fert_p_superphosphate"
    mono_ammonium_phosphate="fert_p_mono_ammonium_phosphate"
    di_ammonium_phosphate="fert_p_di_ammonium_phosphate"
    an_phosphate="fert_p_an_phosphate"
    hypophosphate_raw_phosphate="fert_p_hypophosphate_raw_phosphate"
    ground_basic_slag="fert_p_ground_basic_slag"


class KFertiliserType(Enum):
    potassium_salt="fert_k_potassium_salt"
    potassium_sulphate="fert_k_potassium_sulphate"
    potassium_nitrate="fert_k_potassium_nitrate"
    patent_potassium="fert_k_patent_potassium"


class CaFertiliserType(Enum):
    ca_limestone="fert_ca_limestone"
    ca_carbonation_limestone="fert_ca_carbonation_limestone"
    ca_seaweed_limestone="fert_ca_seaweed_limestone"


class ZnFertiliserType(Enum):
    zn_zinc_sulfate = "fert_zn_zinc_sulfate"
    zn_zinc_oxide = "fert_zn_zinc_oxide"
    zn_other = "fert_zn_other"

class FertModel(object):
    """Inputs:
      n_fertiliser_quantities: map NFertiliserType -> kg N/ha
      p_fertiliser_quantities: map PFertiliserType -> kg P2O5/ha
      k_fertiliser_quantities: map KFertiliserType -> kg K2O/ha
      ca_fertiliser_quantities: map CaFertiliserType -> kg Ca/ha
      zn_fertiliser_quantities: map ZnFertiliserType -> kg Zn/ha
      soil_with_ph_under_or_7: ratio
      climate_zone_1: text
      cultivation_type: text

    Outputs:
      computeNH3:
        nh3_total_mineral_fert: kg NH3/ha
      computeHeavyMetal:
        hm_total_mineral_fert : map HeavyMetalType -> mg i/ha (i:hm type)
    """

    _input_variables = ["n_fertiliser_quantities",
                        "p_fertiliser_quantities",
                        "k_fertiliser_quantities",
                        "ca_fertiliser_quantities",
                        "zn_fertiliser_quantities",
                        "soil_with_ph_under_or_7",
                        "climate_zone_1",
                        "cultivation_type"
                        ]

    _EF_NH3N_MIN_N_FERT_PH_UNDER_OR_SEVEN = {"cool_climate": {
        NFertiliserType.ammonium_nitrate: 0.01,
        NFertiliserType.urea: 0.13,
        NFertiliserType.ureaAN: 0.08,
        NFertiliserType.mono_ammonium_phosphate: 0.04,
        NFertiliserType.di_ammonium_phosphate: 0.04,
        NFertiliserType.an_phosphate: 0.04,  # Other complex NK, NPK fertilizers
        NFertiliserType.lime_ammonium_nitrate: 0.01,  # Calcium ammonium nitrate (CAN)
        NFertiliserType.ammonium_sulphate: 0.07,
        NFertiliserType.potassium_nitrate: 0.04,  # Other complex NK, NPK fertilizers
        NFertiliserType.ammonia_liquid: 0.02  # Anhydrous ammonia
    }, "temperate_climate": {
        NFertiliserType.ammonium_nitrate: 0.01,
        NFertiliserType.urea: 0.13,
        NFertiliserType.ureaAN: 0.08,
        NFertiliserType.mono_ammonium_phosphate: 0.04,
        NFertiliserType.di_ammonium_phosphate: 0.04,
        NFertiliserType.an_phosphate: 0.06,  # Other complex NK, NPK fertilizers
        NFertiliserType.lime_ammonium_nitrate: 0.01,  # Calcium ammonium nitrate (CAN)
        NFertiliserType.ammonium_sulphate: 0.08,
        NFertiliserType.potassium_nitrate: 0.06,  # Other complex NK, NPK fertilizers
        NFertiliserType.ammonia_liquid: 0.02  # Anhydrous ammonia
    }, "warm_climate": {
        NFertiliserType.ammonium_nitrate: 0.02,
        NFertiliserType.urea: 0.16,
        NFertiliserType.ureaAN: 0.10,
        NFertiliserType.mono_ammonium_phosphate: 0.05,
        NFertiliserType.di_ammonium_phosphate: 0.05,
        NFertiliserType.an_phosphate: 0.05,  # Other complex NK, NPK fertilizers
        NFertiliserType.lime_ammonium_nitrate: 0.01,  # Calcium ammonium nitrate (CAN)
        NFertiliserType.ammonium_sulphate: 0.09,
        NFertiliserType.potassium_nitrate: 0.05,  # Other complex NK, NPK fertilizers
        NFertiliserType.ammonia_liquid: 0.02  # Anhydrous ammonia
    }}

    _EF_NH3N_MIN_N_FERT_PH_OVER_SEVEN = {"cool_climate": {
        NFertiliserType.ammonium_nitrate: 0.03,
        NFertiliserType.urea: 0.14,
        NFertiliserType.ureaAN: 0.08,
        NFertiliserType.mono_ammonium_phosphate: 0.07,
        NFertiliserType.di_ammonium_phosphate: 0.07,
        NFertiliserType.an_phosphate: 0.07,  # Other complex NK, NPK fertilizers
        NFertiliserType.lime_ammonium_nitrate: 0.01,  # Calcium ammonium nitrate (CAN)
        NFertiliserType.ammonium_sulphate: 0.14,
        NFertiliserType.potassium_nitrate: 0.07,  # Other complex NK, NPK fertilizers
        NFertiliserType.ammonia_liquid: 0.03  # Anhydrous ammonia
    }, "temperate_climate": {
        NFertiliserType.ammonium_nitrate: 0.03,
        NFertiliserType.urea: 0.14,
        NFertiliserType.ureaAN: 0.08,
        NFertiliserType.mono_ammonium_phosphate: 0.08,
        NFertiliserType.di_ammonium_phosphate: 0.08,
        NFertiliserType.an_phosphate: 0.08,  # Other complex NK, NPK fertilizers
        NFertiliserType.lime_ammonium_nitrate: 0.01,  # Calcium ammonium nitrate (CAN)
        NFertiliserType.ammonium_sulphate: 0.14,
        NFertiliserType.potassium_nitrate: 0.08,  # Other complex NK, NPK fertilizers
        NFertiliserType.ammonia_liquid: 0.03  # Anhydrous ammonia
    }, "warm_climate": {
        NFertiliserType.ammonium_nitrate: 0.03,
        NFertiliserType.urea: 0.17,
        NFertiliserType.ureaAN: 0.10,
        NFertiliserType.mono_ammonium_phosphate: 0.10,
        NFertiliserType.di_ammonium_phosphate: 0.10,
        NFertiliserType.an_phosphate: 0.10,  # Other complex NK, NPK fertilizers
        NFertiliserType.lime_ammonium_nitrate: 0.02,  # Calcium ammonium nitrate (CAN)
        NFertiliserType.ammonium_sulphate: 0.17,
        NFertiliserType.potassium_nitrate: 0.10,  # Other complex NK, NPK fertilizers
        NFertiliserType.ammonia_liquid: 0.04  # Anhydrous ammonia
    }}

    #src: WLFDB Guidelines, tab. 23; Agribalyse methodology report v1.1 Table 80 for Hg
    # mg/kg nutrient
    _HM_N_FERT_VALUES = {NFertiliserType.ammonium_nitrate: [0.18, 25.45, 181.82, 6.91, 47.27, 14.55, 0.36],
                         NFertiliserType.urea: [0.11, 13.04, 95.65, 2.39, 4.35, 4.35, 0.43],
                         NFertiliserType.ureaAN: [0.11, 13.04, 95.65, 2.39, 4.35, 4.35, 0.43], #urea
                         NFertiliserType.mono_ammonium_phosphate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81, 0.4],  #generic
                         NFertiliserType.di_ammonium_phosphate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81, 0.4],  #generic
                         NFertiliserType.an_phosphate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81, 0.4],  #generic
                         NFertiliserType.lime_ammonium_nitrate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81, 0.4],  #generic
                         NFertiliserType.ammonium_sulphate: [0.24, 19.05, 142.86, 5.24, 8.57, 9.52, 0.0],
                         NFertiliserType.potassium_nitrate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81, 0.4],  #generic
                         NFertiliserType.ammonia_liquid: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81, 0.4]  #generic
                        }

    #src: WLFDB Guidelines, tab. 23; Agribalyse methodology report v1.1 Table 80 for Hg
    # mg/kg nutrient
    _HM_P_FERT_VALUES = {PFertiliserType.triple_superphosphate: [113.04, 97.83, 650.00, 7.61, 95.65, 567.39, 0.26],
                         PFertiliserType.superphosphate: [52.63, 121.05, 852.63, 578.95, 105.26, 342.11, 0.58],
                         PFertiliserType.mono_ammonium_phosphate: [51.32, 118.22, 751.32, 49.42, 100.46, 589.46, 0.46],  #generic
                         PFertiliserType.di_ammonium_phosphate: [51.32, 118.22, 751.32, 49.42, 100.46, 589.46, 0.46],  #generic
                         PFertiliserType.an_phosphate: [51.32, 118.22, 751.32, 49.42, 100.46, 589.46, 0.46],  #generic
                         PFertiliserType.hypophosphate_raw_phosphate: [50.00, 115.38, 915.38, 23.85, 76.92, 611.54, 0.5], #P hyperphosphate (raw phosphate, kg P2O5)
                         PFertiliserType.ground_basic_slag: [1.56, 250.00, 425.00, 75.00, 125.00, 12212.50, 0.42] # thomas Meal
                        }

    #src: WLFDB Guidelines, tab. 23; Agribalyse methodology report v1.1 Table 80 for Hg
    # mg/kg nutrient
    _HM_K_FERT_VALUES = {KFertiliserType.potassium_salt: [0.10, 8.33, 76.67, 9.17, 3.50, 3.33, 0.08], #Potassium chloride
                        KFertiliserType.potassium_sulphate: [0.10, 4.00, 64.00, 6.60, 1.60, 4.00, 0.14],
                        KFertiliserType.potassium_nitrate: [0.11, 6.17, 70.33, 7.88, 7.52, 88.54, 0.11], #generic
                        KFertiliserType.patent_potassium: [0.19, 173.08, 153.85, 11.54, 11.54, 173.08, 0.11] #Raw potassium
                        }

    #src: WLFDB Guidelines, tab. 23; Agribalyse methodology report v1.1 Table 80 for Hg
    # mg/kg nutrient
    _HM_CA_FERT_VALUES = {CaFertiliserType.ca_limestone: [0.12, 4.00, 8.00, 3.60, 12.20, 314.00, 0.45],  # Lime kg CaO
                          # Lime kg CaO
                          CaFertiliserType.ca_carbonation_limestone: [0.12, 4.00, 8.00, 3.60, 12.20, 314.00, 0.45],
                          # Lime kg CaO
                          CaFertiliserType.ca_seaweed_limestone: [0.12, 4.00, 8.00, 3.60, 12.20, 314.00, 0.45]
                          }

    _HM_ZN_FERT_VALUES = {ZnFertiliserType.zn_zinc_sulfate: [0.0, 0.0, 1000000.0, 0.0, 0.0, 0.0, 0.0],
                          ZnFertiliserType.zn_zinc_oxide: [0.0, 0.0, 1000000.0, 0.0, 0.0, 0.0, 0.0],
                          ZnFertiliserType.zn_other: [0.0, 0.0, 0.0, 1000000.0, 0.0, 0.0, 0.0]
                          }

    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in FertModel._input_variables:
            setattr(self, key, inputs[key])

    def computeNH3(self):
        return N_TO_NH3_FACTOR * sum(self._compute_nh3_as_n().values())

    def _compute_nh3_as_n(self):
        return {fertKey: fertValue * self._compute_nh3_as_n_for_fert(fertKey) for fertKey, fertValue in
                self.n_fertiliser_quantities.items()}

    def _compute_nh3_as_n_for_fert(self,fert):
        climate = self.climate_zone_1
        soil_ph_under_or_7 = self.soil_with_ph_under_or_7
        if self.cultivation_type.startswith("greenhouse_hydroponic"):
            climate = "temperate_climate"
            soil_ph_under_or_7 = 1.0
        Ef_low_ph = self._EF_NH3N_MIN_N_FERT_PH_UNDER_OR_SEVEN[climate][fert]
        Ef_high_ph = self._EF_NH3N_MIN_N_FERT_PH_OVER_SEVEN[climate][fert]
        return soil_ph_under_or_7 * Ef_low_ph + (1.0 - soil_ph_under_or_7) * Ef_high_ph

    def computeHeavyMetal(self):
        total_hm_values = dict.fromkeys(HeavyMetalType,0.0)
        self._add_hm_values_for_fert_type(total_hm_values, self.n_fertiliser_quantities, self._HM_N_FERT_VALUES)
        self._add_hm_values_for_fert_type(total_hm_values, self.p_fertiliser_quantities, self._HM_P_FERT_VALUES)
        self._add_hm_values_for_fert_type(total_hm_values, self.k_fertiliser_quantities, self._HM_K_FERT_VALUES)
        self._add_hm_values_for_fert_type(total_hm_values, self.ca_fertiliser_quantities, self._HM_CA_FERT_VALUES)
        self._add_hm_values_for_fert_type(total_hm_values, self.zn_fertiliser_quantities, self._HM_ZN_FERT_VALUES)
        return total_hm_values

    def _add_hm_values_for_fert_type(self,total_hm_values, fert_quantities,fert_hm_map):
        for fertKey,fertQuantity in fert_quantities.items():
            for hm_element_index,hm_element_value in enumerate(fert_hm_map[fertKey]):
                total_hm_values[HeavyMetalType(hm_element_index)] += hm_element_value * fertQuantity
