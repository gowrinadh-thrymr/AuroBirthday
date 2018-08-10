package com.auro.serviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.auro.bean.HolidayBean;
import com.auro.service.CalendarService;
import com.ibm.workplace.wcm.api.Content;
import com.ibm.workplace.wcm.api.ContentComponent;
import com.ibm.workplace.wcm.api.DateComponent;
import com.ibm.workplace.wcm.api.DocumentIdIterator;
import com.ibm.workplace.wcm.api.DocumentIterator;
import com.ibm.workplace.wcm.api.DocumentTypes;
import com.ibm.workplace.wcm.api.Repository;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.TextComponent;
import com.ibm.workplace.wcm.api.WCM_API;
import com.ibm.workplace.wcm.api.Workspace;
import com.ibm.workplace.wcm.api.exceptions.AuthorizationException;
import com.ibm.workplace.wcm.api.exceptions.ComponentNotFoundException;
import com.ibm.workplace.wcm.api.exceptions.DocumentRetrievalException;
import com.ibm.workplace.wcm.api.exceptions.OperationFailedException;
import com.ibm.workplace.wcm.api.exceptions.ServiceNotAvailableException;

@Service
public class CalenderServiceImpl implements CalendarService {

	public Workspace getWorkSpace() {
		// retrieve repository
		Repository repository = WCM_API.getRepository();
		// get contentWorkSpace
		Workspace contentWorkSpace = null;
		try {
			contentWorkSpace = repository.getWorkspace();
		} catch (ServiceNotAvailableException | OperationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return contentWorkSpace;

	}

	public List<SiteArea> getHolidaysSiteArea(Workspace contentWorkSpace) {

		String siteAreaName = "Holiday List";
		String libraryName = "Aurobindo_ContentLib";
		List<SiteArea> siteAreas = null;
		if (contentWorkSpace != null) {
			siteAreas = new ArrayList<SiteArea>();
			// Set library
			try {
				contentWorkSpace.setCurrentDocumentLibrary(contentWorkSpace.getDocumentLibrary(libraryName.trim()));
			} catch (Exception e1) {
				e1.printStackTrace();
				System.err.println("error setting the current library to " + libraryName);
			}

			// find sitearea by name
			DocumentIdIterator siteAreaIterator = null;
			try {
				siteAreaIterator = contentWorkSpace.findByName(DocumentTypes.SiteArea, siteAreaName);
			} catch (Exception e1) {
				e1.printStackTrace();
				System.err.println("error finding the siteArea " + siteAreaName);
			}

			SiteArea siteArea = null;
			if (siteAreaIterator != null) {
				if (siteAreaIterator.hasNext()) {
					try {
						siteArea = (SiteArea) contentWorkSpace.getById(siteAreaIterator.nextId());
						if (siteArea != null) {
							siteAreas.add(siteArea);
						}
					} catch (DocumentRetrievalException | AuthorizationException e) {
						e.printStackTrace();
					}
				}
			}

		}

		return siteAreas;
	}

	public LinkedList<HolidayBean> getContent(Workspace contentWorkSpace, SiteArea siteArea) {
		LinkedList<HolidayBean> listObject = new LinkedList<HolidayBean>();
			DocumentIterator it;
			it = contentWorkSpace.getByIds(siteArea.getChildren(),false,true);
			if (it != null) {
				Object obj = null;
				Content content = null;
				while (it.hasNext()) {
					obj = it.next();
					if (obj instanceof Content) {
						content = (Content) obj;
						HolidayBean bean=new HolidayBean();
						ContentComponent dateContent = null;
						try {
							dateContent = (ContentComponent) content.getComponent("Holiday Date");
							DateComponent dateComponent = (DateComponent) dateContent;
							Calendar cal = Calendar.getInstance();
							cal.setTime(dateComponent.getDate());
							int year = cal.get(Calendar.YEAR);
							int month = cal.get(Calendar.MONTH);
							int day = cal.get(Calendar.DAY_OF_MONTH);
							bean.setYear(String.valueOf(year));
							bean.setMonth(String.valueOf(month + 1));
							bean.setDay(String.valueOf(day));
						} catch (ComponentNotFoundException e) {
							e.printStackTrace();
						}
						try {
							ContentComponent occasionContent = (ContentComponent) content.getComponent("Holiday Title");
							TextComponent textComponent = (TextComponent) occasionContent;
							bean.setHolidayName(textComponent.getText().trim());
						} catch (ComponentNotFoundException e) {
							e.printStackTrace();
						}
						listObject.add(bean);
					}
				}
			}

		return listObject;
	}

	@Override
	public List<HolidayBean> getHolidaysByUserLogin(Workspace contentWorkSpace, List<SiteArea> siteAreas, String entity,
			String division, String unit) {
		List<HolidayBean> list=new LinkedList<>();
		
		if(entity!=null && division!=null && unit!=null ) {
			if (entity.equalsIgnoreCase("APL")) {
				if (division.equalsIgnoreCase("API DIVISION")) {
					if (unit.equalsIgnoreCase("HO - API")||unit.equalsIgnoreCase("HO - FDF")) {
						list=getHolidays(contentWorkSpace, siteAreas, entity, division, unit);
					}else {
						//SHOWING GLOBAL SITEAREA HOLIDAYS FOR REMAINING API DIVISION UNITS
						division="GLOBAL HOLIDAYS";
						list=getHolidays(contentWorkSpace, siteAreas, entity, division, unit);
					}
				} else if (division.equalsIgnoreCase("RND2 DIVISION")) {
					if (unit.equalsIgnoreCase("APLRC - I")) {
						list=getHolidays(contentWorkSpace, siteAreas, entity, division, unit);
					}else {
						//SHOWING GLOBAL SITEAREA HOLIDAYS FOR REMAINING RND2 DIVISION UNITS
						division="GLOBAL HOLIDAYS";
						list=getHolidays(contentWorkSpace, siteAreas, entity, division, unit);
					}
				} else if (division.equalsIgnoreCase("RND DIVISION")) {
					if (unit.equalsIgnoreCase("APLRC - II")) {
						list=getHolidays(contentWorkSpace, siteAreas, entity, division, unit);
					}else {
						//SHOWING GLOBAL SITEAREA HOLIDAYS FOR REMAINING RND DIVISION UNITS
						division="GLOBAL HOLIDAYS";
						list=getHolidays(contentWorkSpace, siteAreas, entity, division, unit);
					}
				}else {
					//GET GLOBAL SITEAREA HOLIDAYS
					division="GLOBAL HOLIDAYS";
					list=getHolidays(contentWorkSpace, siteAreas, entity, division, unit);
				}
			}
		}
		

		return list;
	}
	
	
	public List<HolidayBean> getHolidays(Workspace contentWorkSpace, List<SiteArea> siteAreas, String entity, String division,
			String unit) {
		
		List<HolidayBean> list=new LinkedList<>();
		for (int i = 0; i < siteAreas.size(); i++) {
			// Filtering ENTITY
			DocumentIdIterator entities = null;
			DocumentIdIterator divisions = null;
			DocumentIdIterator units = null;
			entities=siteAreas.get(i).getChildren();     //Getting Entities
			while (entities.hasNext()) {
				SiteArea entitySiteArea;
				try {
					entitySiteArea = (SiteArea) contentWorkSpace.getById(entities.nextId());
					if (entitySiteArea != null) {
						if(entitySiteArea.getName().equalsIgnoreCase(entity.trim())) {
						//GETTING DIVISIONS
							divisions=entitySiteArea.getChildren();
							while (divisions.hasNext()) {
								SiteArea divisionSiteArea;
								divisionSiteArea=(SiteArea) contentWorkSpace.getById(divisions.nextId());
								if(divisionSiteArea!=null) {
									if(divisionSiteArea.getName().equalsIgnoreCase(division.trim())) {
										if(division.equalsIgnoreCase("GLOBAL HOLIDAYS")) {
											list=getContent(contentWorkSpace, divisionSiteArea);
										}else {
											//GETTING UNITS
											units=divisionSiteArea.getChildren();
											while (units.hasNext()) {
												SiteArea unitSiteArea;
												unitSiteArea=(SiteArea) contentWorkSpace.getById(units.nextId());
												if(unitSiteArea!=null) {
													if(unitSiteArea.getName().equalsIgnoreCase(unit.trim())) {
														list=getContent(contentWorkSpace, unitSiteArea);
													}
												}
											}
											
										}
									}
								}
							}
						}
//						list=getContent(contentWorkSpace, entitySiteArea);
					}
				} catch (DocumentRetrievalException | AuthorizationException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}


	

	

}
