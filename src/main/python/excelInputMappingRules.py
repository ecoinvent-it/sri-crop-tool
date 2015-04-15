from inputMappings import SimpleInputMappingRule, MapMappingRule,\
    InputAsEnumMappingRule
from models.fertilisermodel import NFertiliserType, PFertiliserType, KFertiliserType, OtherMineralFertiliserType
from models.manuremodel import LiquidManureType, SolidManureType
from models.irrigationmodel import IrrigationType
from models.otherorganicfertilisermodel import OtherOrganicFertiliserType
from models.erosionmodel import TillageMethod, AntiErosionPractice

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

_LIQUID_MANURE_ENUM_TO_FIELD = {
                LiquidManureType.cattle:"manureliquid_cattle",
                LiquidManureType.fattening_pigs:"manureliquid_fattening_pig",
                LiquidManureType.laying_hens:"manureliquid_laying_hen",
                LiquidManureType.sows_and_piglets:"manureliquid_sows_piglet",
                LiquidManureType.other:"manureliquid_other"
                                }

_SOLID_MANURE_ENUM_TO_FIELD = {
                SolidManureType.broiler_litter:"manuresolid_broiler",
                SolidManureType.cattle:"manuresolid_cattle",
                SolidManureType.horses:"manuresolid_horses",
                SolidManureType.laying_hen_litter:"manuresolid_laying_hen",
                SolidManureType.pigs:"manuresolid_pig",
                SolidManureType.sheep_goats:"manuresolid_sheep_goat",
                SolidManureType.other:"manuresolid_other"
                               }

_OTHERORGANIC_ENUM_TO_FIELD = {
                OtherOrganicFertiliserType.compost:"composttype_compost",
                OtherOrganicFertiliserType.meat_and_bone_meal:"composttype_meat_and_bone_meal",
                OtherOrganicFertiliserType.castor_oil_shell_coarse:"composttype_castor_oil_shell_coarse",
                OtherOrganicFertiliserType.vinasse:"composttype_vinasse",
                OtherOrganicFertiliserType.dried_poultry_manure:"composttype_dried_poultry_manure",
                OtherOrganicFertiliserType.stone_meal:"composttype_stone_meal",
                OtherOrganicFertiliserType.feather_meal:"composttype_feather_meal",
                OtherOrganicFertiliserType.horn_meal:"composttype_horn_meal",
                OtherOrganicFertiliserType.horn_shavings_fine:"composttype_horn_shavings_fine",
                OtherOrganicFertiliserType.sewagesludge_liquid:"sewagesludge_liquid",
                OtherOrganicFertiliserType.sewagesludge_dehydrated:"sewagesludge_dehydrated",
                OtherOrganicFertiliserType.sewagesludge_dried:"sewagesludge_dried"
                 }

_IRR_ENUM_TO_FIELD = {
                    IrrigationType.surface_irrigation_no_energy:"ratio_irr_surface_no_energy",
                    IrrigationType.surface_irrigation_electricity:"ratio_irr_surface_electricity",
                    IrrigationType.surface_irrigation_diesel:"ratio_irr_surface_diesel",
                    IrrigationType.sprinkler_irrigation_electricity:"ratio_irr_sprinkler_electricity",
                    IrrigationType.sprinkler_irrigation_diesel:"ratio_irr_sprinkler_diesel",
                    IrrigationType.drip_irrigation_electricity:"ratio_irr_drip_electricity",
                    IrrigationType.drip_irrigation_diesel:"ratio_irr_drip_diesel"
                     }
        
EXCEL_INPUT_MAPPING_RULES = {
                    #Cross-models rules
                    "nitrogen_from_mineral_fert": SimpleInputMappingRule("total_fertnmin"),
                    "p2O5_from_mineral_fert": SimpleInputMappingRule("total_fertpmin"),
                    "water_use_total": SimpleInputMappingRule("total_wateruse"),
                    "drained_part": SimpleInputMappingRule("drainage"),
                    #Fertiliser rules
                    "n_fertiliser_quantities": MapMappingRule(_N_ENUM_TO_FIELD),
                    "p_fertiliser_quantities": MapMappingRule(_P_ENUM_TO_FIELD),
                    "k2O_from_mineral_fert": SimpleInputMappingRule("total_fertkmin"),
                    "k_fertiliser_quantities": MapMappingRule(_K_ENUM_TO_FIELD),
                    "other_mineral_fertiliser_quantities": MapMappingRule(_OTHERMIN_ENUM_TO_FIELD),
                    #Manure rules
                    "liquid_manure_part_before_dilution":  SimpleInputMappingRule("manureliquid_dilution_level"),
                    "liquid_manure_quantities": MapMappingRule(_LIQUID_MANURE_ENUM_TO_FIELD),
                    "solid_manure_quantities": MapMappingRule(_SOLID_MANURE_ENUM_TO_FIELD),
                    #Other organic fertiliser rules
                    "other_organic_fertiliser_quantities": MapMappingRule(_OTHERORGANIC_ENUM_TO_FIELD),
                    #Erosion model rules
                    "tillage_method": InputAsEnumMappingRule("tillage_method", TillageMethod),
                    "anti_erosion_practice": InputAsEnumMappingRule("anti_erosion_practice", AntiErosionPractice),
                    #CO2 model rules
                    "magnesium_from_fertilizer": SimpleInputMappingRule("fert_other_total_mg"),
                    "magnesium_as_dolomite": SimpleInputMappingRule("fert_other_dolomite_in_mg"),
                    #Irrigation rules
                    "irrigation_types_proportions": MapMappingRule(_IRR_ENUM_TO_FIELD),
                    #N model rules
                    #P model rules
                    "p2o5_in_mineral_fertiliser": SimpleInputMappingRule("total_fertpmin"),
                    #HM model rules
                    #pesticides_quantities: TODO map PesticideType -> kg i /(ha*year) (i: pest type)
                   }
