from enum import Enum
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
    
class OtherMineralFertiliserType(Enum):
    ca_limestone="fert_ca_limestone"
    ca_carbonation_linestone="fert_ca_carbonation_linestone"
    ca_seaweed_limestone="fert_ca_seaweed_limestone"


class FertModel(object):
    """Inputs:
      n_fertiliser_quantities: map NFertiliserType -> kg N/ha
      p_fertiliser_quantities: map PFertiliserType -> kg P2O5/ha
      k_fertiliser_quantities: map KFertiliserType -> kg K2O/ha
      other_mineral_fertiliser_quantities: map OtherMinFertiliserType -> kg Ca/ha
      soil_with_ph_under_or_7: ratio

    Outputs:
      computeNH3:
        nh3_total_mineral_fert: kg NH3/ha
      computeHeavyMetal:
        hm_total_mineralfert : map HeavyMetalType -> mg i/ha (i:hm type)
    """
    
    _input_variables = ["n_fertiliser_quantities",
                        "p_fertiliser_quantities",
                        "k_fertiliser_quantities",
                        "other_mineral_fertiliser_quantities",
                        "soil_with_ph_under_or_7"
                       ]
    
    _N = 14.00674
    _H = 1.00794
    _NH3 = _N + 3*_H
    _N_TO_NH3_FACTOR = _NH3/_N #17/14
    
    #src: (EEA 2013, 3D, Table 3-2) time factor 14/17 (Conversion from kg NH3 into kg N)
    _EF_NH3N_MIN_N_FERT_PH_UNDER_OR_SEVEN = {
                               NFertiliserType.ammonium_nitrate: 0.0305,
                               NFertiliserType.urea: 0.2001,
                               NFertiliserType.ureaAN: 0.1029,
                               NFertiliserType.mono_ammonium_phosphate: 0.0931,
                               NFertiliserType.di_ammonium_phosphate: 0.0931,
                               NFertiliserType.an_phosphate: 0.0305, #Other complex NK, NPK fertilizers
                               NFertiliserType.lime_ammonium_nitrate: 0.0181, #Calcium ammonium nitrate (CAN)
                               NFertiliserType.ammonium_sulphate: 0.0107,
                               NFertiliserType.potassium_nitrate: 0.0305, #Other complex NK, NPK fertilizers
                               NFertiliserType.ammonia_liquid:0.0091 #Anhydrous ammonia
                               }
    
    #src: (EEA 2013, 3D, Table 3-2) time factor 14/17 (Conversion from kg NH3 into kg N)
    _EF_NH3N_MIN_N_FERT_PH_OVER_SEVEN = {
                               NFertiliserType.ammonium_nitrate: 0.0305,
                               NFertiliserType.urea: 0.2001,
                               NFertiliserType.ureaAN: 0.1029,
                               NFertiliserType.mono_ammonium_phosphate: 0.2413,
                               NFertiliserType.di_ammonium_phosphate: 0.2413,
                               NFertiliserType.an_phosphate: 0.0305, #Other complex NK, NPK fertilizers
                               NFertiliserType.lime_ammonium_nitrate: 0.0181, #Calcium ammonium nitrate (CAN)
                               NFertiliserType.ammonium_sulphate: 0.2224,
                               NFertiliserType.potassium_nitrate: 0.0305, #Other complex NK, NPK fertilizers
                               NFertiliserType.ammonia_liquid: 0.0091 #Anhydrous ammonia
                               }

    #src: WLFDB Guidelines, tab. 23
    # mg/kg nutrient
    #NOTE: no value for Hg
    _HM_N_FERT_VALUES = {NFertiliserType.ammonium_nitrate: [0.18, 25.45, 181.82, 6.91, 47.27, 14.55],
                         NFertiliserType.urea: [0.11, 13.04, 95.65, 2.39, 4.35, 4.35],
                         NFertiliserType.ureaAN: [0.11, 13.04, 95.65, 2.39, 4.35, 4.35], #urea
                         NFertiliserType.mono_ammonium_phosphate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81],  #generic
                         NFertiliserType.di_ammonium_phosphate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81],  #generic
                         NFertiliserType.an_phosphate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81],  #generic
                         NFertiliserType.lime_ammonium_nitrate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81],  #generic
                         NFertiliserType.ammonium_sulphate: [0.24, 19.05, 142.86, 5.24, 8.57, 9.52],
                         NFertiliserType.potassium_nitrate: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81],  #generic
                         NFertiliserType.ammonia_liquid: [0.21, 22.25, 121.43, 5.37, 17.17, 7.81]  #generic
                        }

    #src: WLFDB Guidelines, tab. 23
    # mg/kg nutrient
    #NOTE: no value for Hg
    _HM_P_FERT_VALUES = {PFertiliserType.triple_superphosphate: [113.04, 97.83, 650.00, 7.61, 95.65, 567.39],
                         PFertiliserType.superphosphate: [52.63, 121.05, 852.63, 578.95, 105.26, 342.11],
                         PFertiliserType.mono_ammonium_phosphate: [51.32, 118.22, 751.32, 49.42, 100.46, 589.46],  #generic
                         PFertiliserType.di_ammonium_phosphate: [51.32, 118.22, 751.32, 49.42, 100.46, 589.46],  #generic
                         PFertiliserType.an_phosphate: [51.32, 118.22, 751.32, 49.42, 100.46, 589.46],  #generic
                         PFertiliserType.hypophosphate_raw_phosphate: [50.00, 115.38, 915.38, 23.85, 76.92, 611.54], #P hyperphosphate (raw phosphate, kg P2O5)
                         PFertiliserType.ground_basic_slag: [1.56, 250.00, 425.00, 75.00, 125.00, 12212.50] # thomas Meal
                        }
    
    #src: WLFDB Guidelines, tab. 23
    # mg/kg nutrient
    #NOTE: no value for Hg
    _HM_K_FERT_VALUES = {KFertiliserType.potassium_salt: [0.10, 8.33, 76.67, 9.17, 3.50, 3.33], #Potassium chloride
                        KFertiliserType.potassium_sulphate: [0.10, 4.00, 64.00, 6.60, 1.60, 4.00],
                        KFertiliserType.potassium_nitrate: [0.11, 6.17, 70.33, 7.88, 7.52, 88.54], #generic
                        KFertiliserType.patent_potassium: [0.19, 173.08, 153.85, 11.54, 11.54, 173.08] #Raw potassium
                        }
    
    #src: WLFDB Guidelines, tab. 23
    # mg/kg nutrient
    #NOTE: no value for Hg
    _HM_OTHER_MINERAL_FERT_VALUES = {OtherMineralFertiliserType.ca_limestone: [0.12, 4.00, 8.00, 3.60, 12.20, 314.00], #Lime kg CaO
                                OtherMineralFertiliserType.ca_carbonation_linestone: [0.12, 4.00, 8.00, 3.60, 12.20, 314.00], #Lime kg CaO
                                OtherMineralFertiliserType.ca_seaweed_limestone: [0.12, 4.00, 8.00, 3.60, 12.20, 314.00] #Lime kg CaO
                                }
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in FertModel._input_variables:
            setattr(self, key, inputs[key])
        
    def computeNH3(self):
        return self._N_TO_NH3_FACTOR * sum(self._compute_nh3_as_n().values())
         
    def _compute_nh3_as_n(self):
        return {fertKey:fertValue * self._compute_nh3_as_n_for_fert(fertKey) for fertKey,fertValue in  self.n_fertiliser_quantities.items()}
         
    def _compute_nh3_as_n_for_fert(self,fert):
        Ef_low_ph = self._EF_NH3N_MIN_N_FERT_PH_UNDER_OR_SEVEN[fert];
        Ef_high_ph = self._EF_NH3N_MIN_N_FERT_PH_OVER_SEVEN[fert];
        return self.soil_with_ph_under_or_7 * Ef_low_ph + (1.0-self.soil_with_ph_under_or_7) * Ef_high_ph
    
    def computeHeavyMetal(self):
        total_hm_values = dict.fromkeys(HeavyMetalType,0.0)
        self._add_hm_values_for_fert_type(total_hm_values, self.n_fertiliser_quantities, self._HM_N_FERT_VALUES);
        self._add_hm_values_for_fert_type(total_hm_values, self.p_fertiliser_quantities, self._HM_P_FERT_VALUES);
        self._add_hm_values_for_fert_type(total_hm_values, self.k_fertiliser_quantities, self._HM_K_FERT_VALUES);
        self._add_hm_values_for_fert_type(total_hm_values, self.other_mineral_fertiliser_quantities, self._HM_OTHER_MINERAL_FERT_VALUES);
        return {'hm_total_mineralfert':total_hm_values}
    
    def _add_hm_values_for_fert_type(self,total_hm_values, fert_quantities,fert_hm_map):
        for fertKey,fertQuantity in fert_quantities.items():
            for hm_element_index,hm_element_value in enumerate(fert_hm_map[fertKey],1):
                total_hm_values[HeavyMetalType(hm_element_index)] += hm_element_value * fertQuantity

    
