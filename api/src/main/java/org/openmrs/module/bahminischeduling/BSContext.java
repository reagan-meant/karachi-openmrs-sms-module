package org.openmrs.module.bahminischeduling;

import org.openmrs.module.bahminischeduling.api.BahminischedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BSContext {
	
	private static BahminischedulingService bahminischedulingService;
	
	@Autowired
	public BSContext(BahminischedulingService bahminischedulingService) {
		BSContext.bahminischedulingService = bahminischedulingService;
	}
	
	public static BahminischedulingService getBahminischedulingService() {
		return bahminischedulingService;
	}
	
}
