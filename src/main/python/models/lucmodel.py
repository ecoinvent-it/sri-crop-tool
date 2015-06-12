
class LUCModel(object):
    """Inputs:
      crop: code
      country: text
      allocated_time_for_crop: ratio

    Outputs:
      m_co2_CO2_from_fertilisers: kg/ha
    """
    _input_variables = ["crop",
                        "country",
                        "allocated_time_for_crop"
                       ]

    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in LUCModel._input_variables:
            setattr(self, key, inputs[key])
            
    def compute(self):
        formula = self._compute_formula()
        return {'m_LUC_luc_formula': formula}
        
    def _compute_formula(self):
        cropSpecific = self._compute_crop_specific()
        sharedResponsability = self._compute_shared_responsability()
        formula = str(cropSpecific) + " * LUC_crop_specific + " + str(sharedResponsability) + " * (1-LUC_crop_specific)" 
        return formula;
    
    def _compute_crop_specific(self):
        currentCropTable = self._RELATIVE_AREAS_EXPANSION_PER_CROP_PER_COUNTRY[self.crop];
        if (self.country in currentCropTable):
            return currentCropTable[self.country]* self.allocated_time_for_crop
        else:
            return 0.0 #FIXME: Define what to do if not found
    
    def _compute_shared_responsability(self):
        return max(0.0, self._EXPANSION_ALL_CROPS_TOTAL_PER_COUNTRY[self.country]) * self.allocated_time_for_crop
    
    # country averages column AD
    _EXPANSION_ALL_CROPS_TOTAL_PER_COUNTRY = {
   "AR":0.401547621623345,
   "AU":0.361890814949756,
   "BE":0.0122983332853962,
   "BR":0.19991651301809,
   "CA":-0.0291874093708358,
   "CL":-0.203276525909219,
   "CN":0.1285285390175,
   "CO":-0.318982421614764,
   "CR":0.178963819255576,
   "CI":0.217900241615088,
   "EC":0.034024458616913,
   "FI":-0.068415451926527,
   "FR":-0.00667182268718828,
   "DE":-0.0209362756010339,
   "GH":0.462406450959335,
   "HU":-0.0785182247479673,
   "IN":0.0769954146475059,
   "ID":0.329837861818753,
   "IL":-0.105963472608196,
   "IT":-0.293955315349683,
   "KE":0.151613255359893,
   "MX":-0.0540308424493069,
   "NL":-0.0998174717717973,
   "NZ":0.0154905017610717,
   "PE":0.434543162777796,
   "PH":0.0824721702746796,
   "PL":-0.171964633660617,
   "RU":-0.402508519596759,
   "ZA":-0.437618268073323,
   "ES":-0.198380120867365,
   "LK":0.0613484049450382,
   "CH":-0.252029149909823,
   "TH":0.151327438916347,
   "TR":-0.154835862026395,
   "UA":0.151475084796927,
   "US":-0.0132376298239295,
   "VN":0.299817925374993
}
    
    _RELATIVE_AREAS_EXPANSION_PER_CROP_PER_COUNTRY = {
        "almond":{"AR": 0.5}#FIXME: This is a test value
     }
