class NonStrictInputMapping(object):
    def __init__(self, originalInputs, mappingRules):
        self.originalInputs = originalInputs
        self._mappingRules = mappingRules
        
    def __getattr__(self, name):
        return self._mappingRules.get(name, IDENTITY_INPUT_MAPPING_RULE).mapField(name, self)
    
class InputMappingRule(object):
    def mapField(self, attrName, mapping):
        raise "Please override this method"
    
class IdentityInputMappingRule(InputMappingRule):
    def mapField(self, attrName, mapping):
        return mapping.originalInputs.get(attrName, None)
    
IDENTITY_INPUT_MAPPING_RULE = IdentityInputMappingRule()
    
class SimpleInputMappingRule(InputMappingRule):
    def __init__(self, otherParam):
        self._otherParam = otherParam
        
    def mapField(self, attrName, mapping):
        return mapping.originalInputs.get(self._otherParam, None)
