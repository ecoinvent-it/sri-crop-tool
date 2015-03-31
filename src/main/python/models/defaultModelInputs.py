from models.pmodel import LandUseCategory
from models.manuremodel import LiquidManureType
from models.manuremodel import SolidManureType
from models.irrigationmodel import IrrigationType

No3Model_defaults = {"bulk_density_of_soil": 1300,
                     "c_per_n_ratio": 11,
                     "clay_content": 0.19,
                     "considered_soil_volume": 5000,
                     "drained_part": 0,
                     "fertilisers_gas_losses": 0.1,
                     "organic_carbon_content": 0.012,
                     "irrigation": 0,
                     "nitrogen_from_fertiliser": 0,
                     "nitrogen_uptake_by_crop": 0,
                     "norg_per_ntotal_ratio": 0.85,
                     "precipitation": 1000,
                     "rooting_depth": 1
                    }

PModel_defaults = {"drained_part": 0.0,
                   "eroded_reaching_river": 0.2,
                   "eroded_soil": 0.0,
                   "eroded_soil_p_enrichment": 1.86,
                   "land_use_category": LandUseCategory.arable_land,
                   "p2o5_in_liquid_manure": 0.0,
                   "p2o5_in_liquid_sludge": 0.0,
                   "p2o5_in_mineral_fertiliser": 0.0,
                   "p2o5_in_solide_manure": 0.0,
                   "p_content_in_soil": 0.00095
                  }

#FIXME: Values
ManureModel_defaults = {"liquid_manure_part_before_dilution":0.5,
                        "liquid_manure_proportions":
                          {LiquidManureType.cattle: 0.2,
                           LiquidManureType.fattening_pigs: 0.2,
                           LiquidManureType.laying_hens: 0.2,
                           LiquidManureType.sows_and_piglets: 0.2,
                           LiquidManureType.other: 0.2
                          },
                        
                        "solid_manure_proportions":
                          {SolidManureType.broiler_litter: 0.2,
                           SolidManureType.cattle: 0.2,
                           SolidManureType.horses: 0.2,
                           SolidManureType.laying_hen_litter: 0.1,
                           SolidManureType.pigs: 0.1,
                           SolidManureType.sheep_goats: 0.1,
                           SolidManureType.other: 0.1
                          },
                        "total_liquid_manure": 0.0,
                        "total_solid_maure": 0.0
                       }

Co2Model_defaults = {"nitrogen_from_urea": 0.0,
                     "nitrogen_from_ureaAN": 0.0,
                     "part_of_urea_in_UAN": 0.5,
                     "calcium_from_lime": 0.0,
                     "calcium_from_carbonation_lime": 0.0,
                     "calcium_from_seaweed_lime": 0.0,
                     "magnesium_from_fertilizer": 0.0,
                     "magnesium_as_dolomite": 1.0
                    }

N2oxModel_defaults = {"nitrogen_from_all_manure": 0.0,
                      "nitrogen_from_mineral_fert": 0.0,
                      "nitrogen_from_crop_residues": 0.0,
                      "nitrate_to_groundwater": 0.0,
                      "nitrate_to_surfacewater": 0.0,
                      "ammonia_due_to_mineral_fert": 0.0,
                      "ammonia_due_to_liquid_manure":0.0,
                      "ammonia_due_to_solid_manure": 0.0
                     }

IrrigationModel_defaults = {"water_use_total":0.0,
                            "irrigation_types_proportions":#Europe
                                {   IrrigationType.surface_irrigation_no_energy:0.0,
                                    IrrigationType.surface_irrigation_electricity:0.02,
                                    IrrigationType.surface_irrigation_diesel:0.00,
                                    IrrigationType.sprinkler_irrigation_electricity:0.97,
                                    IrrigationType.sprinkler_irrigation_diesel:0.00,
                                    IrrigationType.drip_irrigation_electricity:0.01,
                                    IrrigationType.drip_irrigation_diesel:0.00
                                 }
                            }
