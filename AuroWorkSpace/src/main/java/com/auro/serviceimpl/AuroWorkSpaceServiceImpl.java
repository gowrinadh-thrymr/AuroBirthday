package com.auro.serviceimpl;

import java.awt.TextComponent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.auro.bean.AuroWorkSpaceBean;
import com.auro.service.AuroWorkSpaceService;
import com.ibm.workplace.wcm.api.Content;
import com.ibm.workplace.wcm.api.ContentComponent;
import com.ibm.workplace.wcm.api.DocumentIdIterator;
import com.ibm.workplace.wcm.api.DocumentIterator;
import com.ibm.workplace.wcm.api.DocumentTypes;
import com.ibm.workplace.wcm.api.LinkComponent;
import com.ibm.workplace.wcm.api.NumericComponent;
import com.ibm.workplace.wcm.api.Repository;
import com.ibm.workplace.wcm.api.RichTextComponent;
import com.ibm.workplace.wcm.api.ShortTextComponent;
import com.ibm.workplace.wcm.api.SiteArea;
import com.ibm.workplace.wcm.api.WCM_API;
import com.ibm.workplace.wcm.api.Workspace;
import com.ibm.workplace.wcm.api.exceptions.AuthorizationException;
import com.ibm.workplace.wcm.api.exceptions.ComponentNotFoundException;
import com.ibm.workplace.wcm.api.exceptions.DocumentRetrievalException;
import com.ibm.workplace.wcm.api.exceptions.OperationFailedException;
import com.ibm.workplace.wcm.api.exceptions.ServiceNotAvailableException;

@Service
public class AuroWorkSpaceServiceImpl implements AuroWorkSpaceService {

	private final String siteAreaName = "SA_AuroWorkSpace";
	private final String libraryName = "Aurobindo_ContentLib";

	@Override
	public Workspace getWorkSpace() {
		// retrieve repository
		Repository repository = WCM_API.getRepository();
		// get contentWorkSpace
		Workspace contentWorkSpace = null;
		try {
			contentWorkSpace = repository.getWorkspace();
		} catch (ServiceNotAvailableException | OperationFailedException e) {
			e.printStackTrace();
		}
		return contentWorkSpace;
	}

	@Override
	public List<SiteArea> getEntitySiteArea(Workspace contentWorkSpace) {
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
			DocumentIdIterator entity = null;
			try {
				entity = contentWorkSpace.findByName(DocumentTypes.SiteArea, siteAreaName);
			} catch (Exception e1) {
				e1.printStackTrace();
				System.err.println("error finding the siteArea " + siteAreaName);
			}

			SiteArea siteArea = null;
			if (entity != null) {
				if (entity.hasNext()) {
					try {
						siteArea = (SiteArea) contentWorkSpace.getById(entity.nextId());
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

	@Override
	public List<AuroWorkSpaceBean> getAuroWorkSpacebyUserLogin(Workspace contentWorkSpace,List<SiteArea> siteAreas,String entity,String division,String unit) {
		// TODO Auto-generated method stub
		List<AuroWorkSpaceBean> list=new LinkedList<>();
		for (int i = 0; i < siteAreas.size(); i++) {
//					Filtering ENTITY
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
											//GETTING UNITS
											units=divisionSiteArea.getChildren();
											while (units.hasNext()) {
												SiteArea unitSiteArea;
												unitSiteArea=(SiteArea) contentWorkSpace.getById(units.nextId());
												if(unitSiteArea!=null) {
													if(unitSiteArea.getName().equalsIgnoreCase(unit.trim())) {
														list=getAllContent(contentWorkSpace, unitSiteArea);
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
			
			
			
			
			
			
			
			
/*			if (siteAreas.get(i).getChildren().nextId().getName().equalsIgnoreCase(entity.trim())) {
				SiteArea siteArea = null;
				try {
					siteArea = (SiteArea) contentWorkSpace.getById(siteAreas.get(i).getChildren().nextId());
					if (siteArea != null) {
//						Filtering DIVISION
						if (siteArea.getChildren().nextId().getName().equalsIgnoreCase(division.trim())) {
							SiteArea siteArea1 = null;
							try {
								siteArea1 = (SiteArea) contentWorkSpace.getById(siteArea.getChildren().nextId());
								if (siteArea1 != null) {
						//Filtering UNITS
									DocumentIdIterator units = null;
									units=siteArea1.getChildren();
									while (units.hasNext()) {
										SiteArea siteArea2 = (SiteArea) contentWorkSpace.getById(units.nextId());
											if(siteArea2!=null) {
												list=getAllContent(contentWorkSpace, siteArea2);
											}
									}
								}
							} catch (DocumentRetrievalException | AuthorizationException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (DocumentRetrievalException | AuthorizationException e) {
					e.printStackTrace();
				}
			}*/
		}
		return list;
	}
	
	@Override
	public LinkedList<JSONObject> getContent(String name, Workspace contentWorkSpace, List<SiteArea> siteAreas) {

		LinkedList<JSONObject> listObject = new LinkedList<JSONObject>();
		for (SiteArea siteArea : siteAreas) {
			DocumentIterator it = contentWorkSpace.getByIds(siteArea.getChildren(), false, true);
			if (it != null) {
				Object obj = null;
				Content content = null;
				while (it.hasNext()) {
					obj = it.next();
					if (obj instanceof Content) {
						content = (Content) obj;
						if (content.getName().equalsIgnoreCase(name.trim())) {
							// System.out.println("ENTERED INTO CONTENT "+name);
							try {
								JSONObject map = new JSONObject();
								ContentComponent division = (ContentComponent) content.getComponent("Division");
								ShortTextComponent divisionComponent = (ShortTextComponent) division;
								map.put("division", divisionComponent.getText().trim());

								ContentComponent unitId = (ContentComponent) content.getComponent("Unit Id");
								TextComponent unitComponent = (TextComponent) unitId;
								map.put("unitId", unitComponent.getText().trim());

								ContentComponent helpdeskEmail = (ContentComponent) content
										.getComponent("HelpDesk Email");
								TextComponent helpdeskEmailComponent = (TextComponent) helpdeskEmail;
								map.put("helpdeskEmail", helpdeskEmailComponent.getText().trim());

								ContentComponent internalCode = (ContentComponent) content
										.getComponent("Internal Code");
								NumericComponent internalCodeComponent = (NumericComponent) internalCode;
								map.put("internalCode", internalCodeComponent.getNumber());

								ContentComponent helpdeskContactNo = (ContentComponent) content
										.getComponent("HelpDesk Contact No");
								TextComponent helpdeskContactNoComponent = (TextComponent) helpdeskContactNo;
								map.put("helpdeskContactNo", helpdeskContactNoComponent.getText().trim());

								ContentComponent isManagerName = (ContentComponent) content
										.getComponent("IS Manager Name");
								TextComponent isManagerNameComponent = (TextComponent) isManagerName;
								map.put("isManagerName", isManagerNameComponent.getText().trim());

								try {
									ContentComponent isManagerMailId = (ContentComponent) content
											.getComponent("IS Manager Mail IDs");
									TextComponent isManagerMailIdComponent = (TextComponent) isManagerMailId;
									map.put("isManagerMailId", isManagerMailIdComponent.getText().trim());
								} catch (Exception e) {
									e.printStackTrace();
									map.put("isManagerMailId", "");
								}

								ContentComponent isManagerMobileNo = (ContentComponent) content
										.getComponent("IS Manager Mobile number");
								TextComponent isManagerMobileNoComponent = (TextComponent) isManagerMobileNo;
								map.put("isManagerMobileNo", isManagerMobileNoComponent.getText().trim());

								ContentComponent ext = (ContentComponent) content.getComponent("Ext");
								TextComponent extComponent = (TextComponent) ext;
								map.put("ext", extComponent.getText().trim());

								ContentComponent unitAddress = (ContentComponent) content.getComponent("Unit Address");
								RichTextComponent unitAddressComponent = (RichTextComponent) unitAddress;
								map.put("unitAddress", unitAddressComponent.getRichText().trim());

								System.out.println("ADDED TO LIST ");
								listObject.add(map);

							} catch (ComponentNotFoundException e) {
								e.printStackTrace();
								System.out.println("EXCEPTION @@@@@ " + e.getMessage());
							}
						}
					}
				}
			}
		}
		return listObject;
	}
	


	@Override
	public List<AuroWorkSpaceBean> getAllContent(Workspace contentWorkSpace, SiteArea siteArea) {
		LinkedList<AuroWorkSpaceBean> listObject = new LinkedList<AuroWorkSpaceBean>();
		System.out.println(siteArea.getName());
			DocumentIterator it = contentWorkSpace.getByIds(siteArea.getChildren(), false, true);
			if (it != null) {
				Object obj = null;
				Content content = null;
				while (it.hasNext()) {
					obj = it.next();
					if (obj instanceof Content) {
						content = (Content) obj;
						try {
							AuroWorkSpaceBean bean = new AuroWorkSpaceBean();
							ContentComponent division = (ContentComponent) content.getComponent("applicationName");
							ShortTextComponent divisionComponent = (ShortTextComponent) division;
							bean.setTitle(divisionComponent.getText().trim());

							ContentComponent unitId = (ContentComponent) content.getComponent("applicationUrl");
							LinkComponent linkComponent = (LinkComponent) unitId;
							bean.setUrl(linkComponent.getURL().trim());

							listObject.add(bean);
						} catch (ComponentNotFoundException e) {
							e.printStackTrace();
							System.out.println("EXCEPTION @@@@@ " + e.getMessage());
						}
					}
				}
		}
		return listObject;
	}


}
