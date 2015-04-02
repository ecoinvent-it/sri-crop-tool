from inputMappings import SimpleInputMappingRule

EXCEL_INPUT_MAPPING_RULES = {
                    #Cross-models rules
                    #CO2 model rules
                    "nitrogen_from_urea": SimpleInputMappingRule("fertnmin_urea"), #FIXME: Replace by mapping that will fetch the n min fert map
                    "nitrogen_from_ureaAN": SimpleInputMappingRule("fertnmin_urea_an"),
                    "calcium_from_lime": SimpleInputMappingRule("fertotherca_limestone"),
                    "calcium_from_carbonation_lime": SimpleInputMappingRule("fertotherca_carbonation_limestone"),
                    "calcium_from_seaweed_lime": SimpleInputMappingRule("fertotherca_seaweed_limestone"),
                    "magnesium_from_fertilizer": SimpleInputMappingRule("fert_other_total_mg"),
                    "magnesium_as_dolomite": SimpleInputMappingRule("TODO"),
                    #NO3 model rules
                    "drained_part": SimpleInputMappingRule("drainage"),
                    "irrigation": "TODO",
                    "nitrogen_from_fertiliser": "TODO",
                    "nitrogen_uptake_by_crop": "TODO",
                    "precipitation": "TODO",
                   }

