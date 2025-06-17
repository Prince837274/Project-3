package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.PositionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.PositionModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "PositionCtl", urlPatterns = { "/ctl/PositionCtl" })
public class  PositionCtl extends BaseCtl{
	@Override
	protected void preload(HttpServletRequest request) {

		
			HashMap<String, String> map = new HashMap();
			map.put("open", "open");
			map.put("closed", "closed");
			map.put("onhold", "onhold");
			
			
			request.setAttribute("con", map);
		    
		}
		protected boolean validate(HttpServletRequest request) {
			boolean pass = true;

			if (DataValidator.isNull(request.getParameter("designation"))) {
				request.setAttribute("designation", PropertyReader.getValue("error.require", "designation"));
				pass = false;


			} else if (!DataValidator.isName(request.getParameter("designation"))) {
				request.setAttribute("designation", " Only numbers are allowed");
				System.out.println(pass);
				pass = false;

			}
			
			/*
				 * else if (!DataValidator.isFloat(request.getParameter("paymentterm"))) {
				 * request.setAttribute("paymentterm", "Only numbers are allowed"); pass =
				 * false; }
				 */
//			if (!OP_UPDATE.equalsIgnoreCase(request.getParameter("operation"))) {
				
				
				if (DataValidator.isNull(request.getParameter("openingDate"))) {
					request.setAttribute("openingDate", PropertyReader.getValue("error.require", "openingDate"));
					pass = false;
				
				}
						
				if (DataValidator.isNull(request.getParameter("requiredexperience"))) {
					request.setAttribute("requiredexperience", PropertyReader.getValue("error.require", "requiredexperience"));
					pass = false;


					if (DataValidator.isNull(request.getParameter("condition"))) {
						request.setAttribute("condition", PropertyReader.getValue("error.require", "condition"));
						pass = false;		
					}
				}
				
					
			
		
				return pass;
	}

//			return pass;
//			}

		protected BaseDTO populateDTO(HttpServletRequest request) {
			 PositionDTO dto = new  PositionDTO();
			
	         
	         
			 dto.setId(DataUtility.getLong(request.getParameter("id")));
			 dto.setOpeningDate(DataUtility.getDate(request.getParameter("openingDate")));
			 dto.setRequiredExperience(DataUtility.getString(request.getParameter("requiredExperience")));
	         
	         dto.setCondition(DataUtility.getString(request.getParameter("condition")));
	         

	        populateBean(dto,request);
			

			return dto;

		}
		
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			
			String op = DataUtility.getString(request.getParameter("operation"));
			PositionModelInt model = ModelFactory.getInstance().getPositionModel();
			long id = DataUtility.getLong(request.getParameter("id"));
			if (id > 0 || op != null) {
				 PositionDTO dto;
				try {
					dto = model.findByPK(id);
					ServletUtility.setDto(dto, request);
				} catch (Exception e) {
					e.printStackTrace();
					ServletUtility.handleException(e, request, response);
					return;
				}
			}
			ServletUtility.forward(getView(), request, response);
		}
		
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
			String op = DataUtility.getString(request.getParameter("operation"));
			PositionModelInt model = ModelFactory.getInstance().getPositionModel();
			long id = DataUtility.getLong(request.getParameter("id"));
			if (OP_SAVE.equalsIgnoreCase(op)||OP_UPDATE.equalsIgnoreCase(op)) {
				 PositionDTO dto = ( PositionDTO) populateDTO(request);
				try {
					if (id > 0) {
						model.update(dto);
						
						ServletUtility.setSuccessMessage("Data is successfully Update", request);
					} else {
						
						try {
							 model.add(dto);
						 
							 ServletUtility.setDto(dto, request);
							ServletUtility.setSuccessMessage("Data is successfully saved", request);
						} catch (ApplicationException e) {
							ServletUtility.handleException(e, request, response);
							return;
						} catch (DuplicateRecordException e) {
							ServletUtility.setDto(dto, request);
							ServletUtility.setErrorMessage("Login id already exists", request);
						}

					}
					ServletUtility.setDto(dto, request);
					
					
				} catch (ApplicationException e) {
					ServletUtility.handleException(e, request, response);
					return;
				} catch (DuplicateRecordException e) {
					ServletUtility.setDto(dto, request);
					ServletUtility.setErrorMessage("Login id already exists", request);
				}
			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				 PositionDTO dto = ( PositionDTO) populateDTO(request);
				try {
					model.delete(dto);
					ServletUtility.redirect(ORSView. POSITION_LIST_CTL, request, response);
					return;
				} catch (ApplicationException e) {
					ServletUtility.handleException(e, request, response);
					return;
				}

			} else if (OP_CANCEL.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView. POSITION_LIST_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView. POSITION_CTL, request, response);
				return;
			}
			ServletUtility.forward(getView(), request, response);

		}
		
		
		
		@Override
		protected String getView() {
		
			return ORSView. POSITION_VIEW;
		}

		


	

	

}
