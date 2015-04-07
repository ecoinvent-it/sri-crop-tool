from inputMappings import SimpleInputMappingRule, MapMappingRule
from models.fertilisermodel import NFertiliserType, PFertiliserType, KFertiliserType, OtherMineralFertiliserType

_N_ENUM_TO_FIELD = {
                NFertiliserType.ammonium_nitrate:"fertnmin_ammonium_nitrate",
                NFertiliserType.urea:"fertnmin_urea",
                NFertiliserType.ureaAN:"fertnmin_urea_an",
                NFertiliserType.mono_ammonium_phosphate:"fertnmin_mono_ammonium_phosphate",
                NFertiliserType.di_ammonium_phosphate:"fertnmin_di_ammonium_phosphate",
                NFertiliserType.an_phosphate:"fertnmin_an_phosphate",
                NFertiliserType.lime_ammonium_nitrate:"fertnmin_lime_ammonium_nitrate",
                NFertiliserType.ammonium_sulphate:"fertnmin_ammonium_sulphate",
                NFertiliserType.potassium_nitrate:"fertnmin_potassium_nitrate",
                NFertiliserType.ammonia_liquid:"fertnmin_ammonia_liquid"
                 }

_P_ENUM_TO_FIELD = {
                PFertiliserType.triple_superphosphate:"fertpmin_triple_superphosphate",
                PFertiliserType.superphosphate:"fertpmin_superphosphate",
                PFertiliserType.mono_ammonium_phosphate:"fertpmin_mono_ammonium_phosphate",
                PFertiliserType.di_ammonium_phosphate:"fertpmin_di_ammonium_phosphate",
                PFertiliserType.an_phosphate:"fertpmin_an_phosphate",
                PFertiliserType.hypophosphate_raw_phosphate:"fertpmin_hypophosphate_raw_phosphate",
                PFertiliserType.ground_basic_slag:"fertpmin_ground_basic_slag"
                 }

_K_ENUM_TO_FIELD = {
                KFertiliserType.potassium_salt:"fertkmin_potassium_salt_kcl",
                KFertiliserType.potassium_sulphate:"fertkmin_potassium_sulphate_k2so4",
                KFertiliserType.potassium_nitrate:"fertkmin_potassium_nitrate",
                KFertiliserType.patent_potassium:"fertkmin_patent_potassium"
                 }

_OTHERMIN_ENUM_TO_FIELD = {
                OtherMineralFertiliserType.ca_limestone:"fertotherca_limestone",
                OtherMineralFertiliserType.ca_carbonation_linestone:"fertotherca_carbonation_limestone",
                OtherMineralFertiliserType.ca_seaweed_limestone:"fertotherca_seaweed_limestone"
                 }
    
EXCEL_INPUT_MAPPING_RULES = {
                    #Cross-models rules
                    #Fertiliser rules
                    "n_fertiliser_quantities": MapMappingRule(_N_ENUM_TO_FIELD),
                    "p_fertiliser_quantities": MapMappingRule(_P_ENUM_TO_FIELD),
                    "k_fertiliser_quantities": MapMappingRule(_K_ENUM_TO_FIELD),
                    "other_mineral_fertiliser_quantities": MapMappingRule(_OTHERMIN_ENUM_TO_FIELD),
                    #CO2 model rules
                    "magnesium_from_fertilizer": SimpleInputMappingRule("fert_other_total_mg"),
                    "magnesium_as_dolomite": SimpleInputMappingRule("fert_other_dolomite_in_mg"),
                    #NO3 model rules
                    "drained_part": SimpleInputMappingRule("drainage"),
                    "irrigation": "TODO",
                    "nitrogen_from_fertiliser": "TODO",
                    "nitrogen_uptake_by_crop": "TODO",
                    "precipitation": "TODO",
                   }

