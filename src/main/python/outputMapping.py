from models.atomicmass import MA_NH3, MA_N

def identity(x): return x

class OutputMapping(object):
    
    def __init__(self):
        self.output = {}
        
    def mapAsIsOutput(self, allInputs):
        self.output["country"] = allInputs["country"]
        self.output["crop"] = allInputs["crop"]
        for k,f in self._DIRECT_OUTPUT_MAPPING.items():
            if (k in allInputs):
                self.output[k] = f(allInputs[k])
        
        self._mapTypeOfDrying(allInputs)
       
    def _mapTypeOfDrying(self,allInputs):
        if (allInputs["type_of_drying"] == "ambient_air"):
            self.output["drying_ambient_air"] = allInputs["computed_drying"]
        elif (allInputs["type_of_drying"] == "heating"):
            self.output["drying_heating"] = allInputs["computed_drying"]
        
    def mapIrrigationModel(self, irrOutput):
        for key, value in irrOutput.items():
            self.output[key.replace("m_Irr_", "")] = value
        self.output["water_to_air"] = self.output["water_to_air"] * 1000.0 #m3/ha -> kg
            
    def mapCo2Model(self, co2Output):
        for key, value in co2Output.items():
            self.output[key.replace("m_co2_", "")] = value
            
    def mapNModel(self, no3Output):
        for key, value in no3Output.items():
            self.output[key.replace("m_N_", "")] = value
            
    def mapErosionModel(self, erosionOutput):
        for key, value in erosionOutput.items():
            self.output[key.replace("m_Erosion_", "")] = value
            
    def mapPModel(self, pOutput):
        for key, value in pOutput.items():
            self.output[key.replace("m_P_", "")] = value
        #TODO: Is this the best place for that?
        self.output["PO4_surfacewater"] = self.output["PO4_surfacewater_drained"] + self.output["PO4_surfacewater_ro"]
    
    def mapFertilizers(self, allInputs):
        self._mapEnumMap(allInputs["n_fertiliser_quantities"])
        self._mapEnumMap(allInputs["p_fertiliser_quantities"])
        self._mapEnumMap(allInputs["k_fertiliser_quantities"])
        self._mapEnumMap(allInputs["other_mineral_fertiliser_quantities"])
        #TODO: Is this the best place for that?
        self.output["fert_n_ammonia_liquid_as_nh3"] = self.output["fert_n_ammonia_liquid"] * MA_NH3/MA_N
        
    def mapOtherOrganicFertilizers(self, allInputs):
        self._mapEnumMap(allInputs["other_organic_fertiliser_quantities"])
       
    def mapHMModel(self,hmOutput):
        for key, hmMap in hmOutput.items():
            prefix = key.replace("m_hm_", "") + "_"
            for k, v in hmMap.items():
                self.output[prefix + k.name] = v
            
    def _mapEnumMap(self, enumdict):
        for k,v in enumdict.items():
            self.output[k.value] = v
            
    _DIRECT_OUTPUT_MAPPING = {
        # FIXME: No system_boundary input anymore, use ecospold name metadata instead?
        #"system_boundary":identity,      
        "record_entry_by":identity,
        "collection_method":identity,
        "data_treatment_extrapolations":identity,
        "data_treatment_uncertainty":identity,
        "comment":identity,
        
        "CO2_from_yield": identity,
        "energy_gross_calorific_value": identity,
        "pest_horticultural_oil": identity,
         #kg diesel -> hr,
        "soilcultivation_decompaction": lambda x: x / 15.921,
        #kg diesel -> ha (except specified),
        "soilcultivation_tillage_chisel": lambda x: x / 15.5,
        "soilcultivation_tillage_spring_tine_weeder": lambda x: x / 1.6,
        "soilcultivation_tillage_rotary_harrow": lambda x: x / 11.5,
        "soilcultivation_tillage_sprint_tine_harrow": lambda x: x / 4.4,
        "soilcultivation_tillage_hoeing_earthing_up": lambda x: x / 3.6 ,
        "soilcultivation_tillage_plough": lambda x: x / 26.1,
        "soilcultivation_tillage_roll": lambda x: x / 3.18,
        "soilcultivation_tillage_rotary_cultivator": lambda x: x / 14.1,
        "sowingplanting_sowing": lambda x: x / 3.82,
        "sowingplanting_planting_seedlings": lambda x: x / 16.8,
        "sowingplanting_planting_trees": lambda x: x / 0.18333, # kg diesel -> p
        "sowingplanting_planting_potatoes": lambda x: x / 8.9,
        "fertilisation_fertilizing_broadcaster": lambda x: x / 5.29,
        "fertilisation_liquid_manure_vaccum_tanker": lambda x: x / 0.217,
        "fertilisation_solid_manure": lambda x: x / 0.000531,
        "harvesting_chopping_maize": lambda x: x / 52.8,
        "harvesting_threshing_combine_harvester": lambda x: x / 33.3,
        "harvesting_picking_up_forage_self_propelled_loader": lambda x: x / 0.106, # kg diesel -> m3
        "harvesting_beets_complete_havester": lambda x: x / 103.0,
        "harvesting_potatoes_complete_havester": lambda x: x / 28.1,
        "harvesting_making_hay_rotary_tedder": lambda x: x / 1.92,
        "harvesting_loading_bales": lambda x: x / 0.0811, #kg diesel -> unit
        "harvesting_mowing_motor_mower": lambda x: x / 3.0,
        "harvesting_mowing_rotary_mower": lambda x: x / 4.31,
        "harvesting_removing_potatoes_haulms": lambda x: x / 4.79,
        "harvesting_windrowing_rotary_swather": lambda x: x / 2.94,
        #"": lambda x: x / 1.0 #FIXME: missing conv factor,
        "plantprotection_spraying": lambda x: x / 1.76,
        
        "otherworkprocesses_baling": lambda x:  x / 0.743, # kg diesel -> unit
        "otherworkprocesses_chopping": lambda x: x / 52.8, #same as harvesting_chopping_maize
        "otherworkprocesses_mulching": lambda x: x / 3.51,
        "otherworkprocesses_transport_tractor_trailer": lambda x: x / 0.0436, # kg diesel -> tkm
        
        "energy_electricity_low_voltage_at_grid": lambda x: x / 3.6, #MJ -> kWh
        "energy_electricity_photovoltaic_produced_locally": lambda x: x / 3.6, #MJ -> kWh
        "energy_electricity_wind_power_produced_locally": lambda x: x / 3.6, #MJ -> kWh
        "energy_diesel_excluding_diesel_used_in_tractor": identity,
        # kg -> MJ
        "energy_lignite_briquette": lambda x: x / 0.111,
        "energy_hard_coal_briquette": lambda x: x / 0.0318,
        "energy_fuel_oil_light": lambda x: x / 0.02342,
        "energy_fuel_oil_heavy": lambda x: x / 0.025,
        "energy_natural_gas": lambda x: x / 0.0272, # Nm3 -> MJ
        "energy_wood_pellets_humidity_10_percent": lambda x: x / 0.0587,
        "energy_wood_chips_fresh_humidity_40_percent": lambda x: x / 0.0545,
        "energy_wood_logs": lambda x: x / 0.0643,
        
        "energy_heat_district_heating": identity,
        "energy_heat_solar_collector": identity,
        
        "materials_fleece": lambda x: x * 0.1, #m2 -> kg,
        "materials_silage_foil": lambda x: x* 0.3, #m2 -> kg,
        "materials_covering_sheet": lambda x: x* 0.3, #m2 -> kg,
        "materials_bird_net": lambda x: x* 0.3, #m2 -> kg,
        
        "greenhouse_plastic_tunnel": identity,
        "greenhouse_glass_roof_metal_tubes": identity,
        "greenhouse_glass_roof_plastic_tubes": identity,
        "greenhouse_plastic_roof_metal_tubes": identity,
        "greenhouse_plastic_roof_plastic_tubes": identity,
    
        "eol_plastic_disposal_fleece_and_other": identity,
        "eol_landfill": identity,
        "eol_incineration": identity,
        "eol_waste_water": identity}
        
