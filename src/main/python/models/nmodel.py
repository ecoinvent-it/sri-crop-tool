from models.atomicmass import NH3_TO_N_FACTOR, NO2_TO_N_FACTOR, N_TO_NO3_FACTOR,\
    N_TO_N2O_FACTOR, NO_TO_N_FACTOR, N_TO_NO2_FACTOR
    
class NModel(object):
    """Inputs:
      ammonia_due_to_manure: kg NH3 / (ha*crop cycle)
      ammonia_due_to_mineral_fert: kg NH3 / (ha*crop cycle)
      ammonia_due_to_other_orga_fert: kg NH3 / (ha*crop cycle)
      bulk_density_of_soil: kg/m3
      c_per_n_ratio: ratio
      clay_content: ratio
      considered_soil_volume: m3/ha
      drained_part: ratio
      nitrogen_from_all_manure: kg N / (ha*crop cycle)
      nitrogen_from_crop_residues: kg N / (ha*crop cycle)
      nitrogen_from_mineral_fert: kg N / (ha*crop cycle)
      nitrogen_from_other_orga_fert: kg N / (ha*crop cycle)
      nitrogen_uptake_by_crop: kg N / (ha*crop cycle)
      norg_per_ntotal_ratio: ratio
      organic_carbon_content: kg C/kg soil,
      precipitation_per_crop_cycle: m3/(ha*crop cycle)
      rooting_depth: m
      water_use_total: m3/(ha*crop cycle)
      
    Outputs:
      m_N_ammonia_total: kg NH3 / (ha*crop cycle)
      m_N_nitrate_to_groundwater: kg NO3 / (ha*crop cycle)
      m_N_nitrate_to_surfacewater: kg NO3 / (ha*crop cycle)
      m_N_N2o_air: kg N2O / (ha*crop cycle)
      m_N_Nox_as_n2o_air: kg NOx as N2O / (ha*crop cycle)
    """
    
    _input_variables = [
                        "ammonia_due_to_manure",
                        "ammonia_due_to_mineral_fert",
                        "ammonia_due_to_other_orga_fert",
                        "bulk_density_of_soil",
                        "c_per_n_ratio",
                        "clay_content",
                        "considered_soil_volume",
                        "drained_part",
                        "nitrogen_from_all_manure",
                        "nitrogen_from_crop_residues",
                        "nitrogen_from_mineral_fert",
                        "nitrogen_from_other_orga_fert",
                        "nitrogen_uptake_by_crop",
                        "norg_per_ntotal_ratio",
                        "organic_carbon_content",
                        "precipitation_per_crop_cycle",
                        "rooting_depth",
                        "water_use_total"
                       ]
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in NModel._input_variables:
            setattr(self, key, inputs[key])
    
    def compute(self):
        total_fert_nitrogen = self._compute_total_fert_nitrogen()
        ammonia_total = self._compute_total_due_ammonia()
        nox = self._compute_nox_as_no2(total_fert_nitrogen)
        gaslosses = self._compute_n_gas_losses(total_fert_nitrogen, ammonia_total, nox)
        no3asNleach = self.computeNO3leachAsN(total_fert_nitrogen, ammonia_total, gaslosses)
        n2o = self._compute_n2o(gaslosses, no3asNleach)
        no3gw, no3sw = self._split_n3oasNleach_between_ground_and_surface_waters(no3asNleach)
        return {"m_N_ammonia_total": ammonia_total,
                "m_N_nitrate_to_groundwater": no3gw,
                "m_N_nitrate_to_surfacewater": no3sw,
                "m_N_N2o_air": n2o,
                "m_N_Nox_as_n2o_air": nox}
        
    def _compute_total_due_ammonia(self):
        return self.ammonia_due_to_mineral_fert + self.ammonia_due_to_manure + self.ammonia_due_to_other_orga_fert;

    def _compute_total_fert_nitrogen(self):
        return self.nitrogen_from_all_manure + self.nitrogen_from_mineral_fert + self.nitrogen_from_other_orga_fert;
        
    def _compute_nox_as_no2(self,total_fert_nitrogen): #0.026 is a ratio in kg NO/kg N
        return total_fert_nitrogen * (0.026 * NO_TO_N_FACTOR) * N_TO_NO2_FACTOR
    
    def _compute_n_gas_losses(self, total_fert_nitrogen, ammonia_total, nox):
        return 0.01 * (total_fert_nitrogen + self.nitrogen_from_crop_residues
                        + NH3_TO_N_FACTOR * ammonia_total + NO2_TO_N_FACTOR * nox)
    
    def computeNO3leachAsN(self, total_fert_nitrogen, ammonia_total, gaslosses):
        corg = self._compute_carbon_in_soil_orga_matter()
        s = self._compute_considered_nitrogen_from_fertiliser(total_fert_nitrogen, ammonia_total, gaslosses)
        norg = self._compute_nitrogen_in_soil_orga_matter(corg)
        return self._compute_nitrogen_leaching(s, norg)
        
    def _compute_carbon_in_soil_orga_matter(self): # kg C/kg soil * kg soil/m3 * m3/ha -> kg C/ha
        return self.organic_carbon_content * self.bulk_density_of_soil * self.considered_soil_volume
    
    def _compute_considered_nitrogen_from_fertiliser(self, total_fert_nitrogen, ammonia_total, gaslosses):
        return total_fert_nitrogen - gaslosses #FIXME: No removal of ammonia?
                
    def _compute_nitrogen_in_soil_orga_matter(self, carbon_in_soil):
        return carbon_in_soil / self.c_per_n_ratio * self.norg_per_ntotal_ratio
    
    def _compute_nitrogen_leaching(self, s, nitrogen_in_soil):
        res = 21.37 + self._compute_all_water_in_mm() \
            / (self.clay_content * 100 * self.rooting_depth) \
            * (0.0037 * s                                    \
                + 0.0000601 * nitrogen_in_soil               \
                - 0.00362 * self.nitrogen_uptake_by_crop)
            
        return max(0, res);
    
    def _compute_all_water_in_mm(self):
        return (self.precipitation_per_crop_cycle + self.water_use_total) * 0.1 # m3/ha -> mm
        
    def _compute_n2o(self, gaslosses, no3asNleach):
        return N_TO_N2O_FACTOR * (gaslosses + 0.0075 * no3asNleach)    
    
    def _split_n3oasNleach_between_ground_and_surface_waters(self, no3asNleach):
        nitrate = no3asNleach * N_TO_NO3_FACTOR
        surface = nitrate * self.drained_part
        ground = nitrate - surface
        return (ground, surface)
        