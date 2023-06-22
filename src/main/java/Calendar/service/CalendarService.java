package Calendar.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import Calendar.model.CalendarObj;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.model.Event;

@Service
public class CalendarService{

	private static final String APPLICATION_NAME = "";
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.calendar.Calendar client;

	private static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

	GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Credential credential;

	@Value("${google.client.client-id}")
	private String clientId;
	@Value("${google.client.client-secret}")
	private String clientSecret;
	@Value("${google.client.redirectUri}")
	private String redirectURI;
	@Value("${google.client.redirectUri.available.slot}")
	private String redirectURIAvailableSlot;

	private Set<Event> events = new HashSet<>();

	private final int START_HOUR = 8;
	private final int START_MIN = 00;
	private final int END_HOUR = 20;
	private final int END_MIN = 00;

	private static boolean isAuthorised = false;

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	private String authorize(String redirectURL) throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if (flow == null) {
			Details web = new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					Collections.singleton(CalendarScopes.CALENDAR)).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURL);

		isAuthorised = true;

		return authorizationUrl.build();
	}

	@RequestMapping(value = "/calendar", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
		return new RedirectView(authorize(redirectURI));
	}

	@RequestMapping(value = "/calendar", method = RequestMethod.GET, params = "code")
	public String oauth2Callback(@RequestParam(value = "code") String code, Model model) {
		if(isAuthorised) {
			try {

				model.addAttribute("title", "Today's Calendar Events (" +START_HOUR+":"+START_MIN +" - "+END_HOUR+":"+END_MIN +")");
				model.addAttribute("calendarObjs", getTodaysCalendarEventList(code, redirectURI));

			} catch (Exception e) {
				model.addAttribute("calendarObjs", new ArrayList<CalendarObj>());
			}

			return getAgendaPage ();
		} else {
			return "/";
		}
	}

	private String getAgendaPage(){
		return "<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head>\n" +
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
				"\n" +
				"    <title th:text=\"${title}\"></title>\n" +
				"    <style>\n" +
				"        .navbar {\n" +
				"            overflow: hidden;\n" +
				"            background-color: #333;\n" +
				"            position: fixed;\n" +
				"            top: 0;\n" +
				"            width: 100%;\n" +
				"        }\n" +
				"\n" +
				"        .navbar a {\n" +
				"            float: right;\n" +
				"            display: block;\n" +
				"            color: #f2f2f2;\n" +
				"            text-align: center;\n" +
				"            padding: 14px 16px;\n" +
				"            text-decoration: none;\n" +
				"            font-size: 17px;\n" +
				"        }\n" +
				"\n" +
				"        .navbar a:hover {\n" +
				"            background: #ddd;\n" +
				"            color: black;\n" +
				"        }\n" +
				"\n" +
				"\n" +
				"        table {\n" +
				"            width: 100%;\n" +
				"            border-collapse: collapse;\n" +
				"            margin: 0 auto;\n" +
				"        }\n" +
				"\n" +
				"        .td1 {\n" +
				"            width:30%;\n" +
				"            padding: 10px 10px 10px 5px;\n" +
				"        }\n" +
				"\n" +
				"        .td2 {\n" +
				"            width:70%;\n" +
				"            padding: 0px 0px 10px 20px;\n" +
				"        }\n" +
				"    </style>\n" +
				"\n" +
				"</head>\n" +
				"<body>\n" +
				"\n" +
				"<div class=\"navbar\">\n" +
				"    <a th:href=\"@{/}\">Logout</a>\n" +
				"    <a th:href=\"@{/slot/available}\">Available Slots</a>\n" +
				"    <a th:href=\"@{/calendar}\">Agenda</a>\n" +
				"    <a th:href=\"@{#}\" style=\"float: left;background: #ddd;color: black;font-weight:bold;\">Spring Boot Application</a>\n" +
				"</div>\n" +
				"\n" +
				"<div style=\"width: 50%;margin: 0 auto;margin-top:120px;padding:20px;border: 1px solid gray;\">\n" +
				"    <h2 style=\"text-align:center;padding-bottom:20px;\" th:text=\"${title}\"></h2>\n" +
				"\n" +
				"    <table>\n" +
				"        <tbody>\n" +
				"\n" +
				"        <tr th:each=\"calendarObj: ${calendarObjs}\">\n" +
				"            <td th:text=\"${calendarObj.startEnd}\" class=\"td1\"></td>\n" +
				"            <td th:text=${calendarObj.title} class=\"td2\"></td>\n" +
				"        </tr>\n" +
				"        </tbody>\n" +
				"    </table>\n" +
				"</div>\n" +
				"\n" +
				"</body>\n" +
				"</html>\n";
	}

	@RequestMapping(value = "/slot/available", method = RequestMethod.GET)
	public RedirectView checkConnectionStatus(HttpServletRequest request, Model model) throws Exception {
		return new RedirectView(authorize(redirectURIAvailableSlot));
	}

	@RequestMapping(value = "/slot/available", method = RequestMethod.GET, params = "code")
	public String oauth2Callback2(@RequestParam(value = "code") String code, Model model) {
		if(isAuthorised) {
			try {

				List<CalendarObj> calendarEventList = getTodaysCalendarEventList(code, redirectURIAvailableSlot);
				List<CalendarObj> freeCalendarObjs = populateAvailableSlot(calendarEventList);

				model.addAttribute("title", "Today's  (" +START_HOUR+":"+START_MIN +" - "+END_HOUR+":"+END_MIN +")");
				model.addAttribute("calendarObjs", freeCalendarObjs);

			} catch (Exception e) {
				model.addAttribute("title", e.getMessage());
				model.addAttribute("calendarObjs", new ArrayList<CalendarObj>());
			}

			return getAvailableSlots ();
		} else {
			return "/";
		}
	}
	private String getAvailableSlots() {
		return "<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head>\n" +
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
				"\n" +
				"<title th:text=\"${title}\"></title>\n" +
				"<style>\n" +
				".navbar {\n" +
				"  overflow: hidden;\n" +
				"  background-color: #333;\n" +
				"  position: fixed;\n" +
				"  top: 0;\n" +
				"  width: 100%;\n" +
				"}\n" +
				"\n" +
				".navbar a {\n" +
				"  float: right;\n" +
				"  display: block;\n" +
				"  color: #f2f2f2;\n" +
				"  text-align: center;\n" +
				"  padding: 14px 16px;\n" +
				"  text-decoration: none;\n" +
				"  font-size: 17px;\n" +
				"}\n" +
				"\n" +
				".navbar a:hover {\n" +
				"  background: #ddd;\n" +
				"  color: black;\n" +
				"}\n" +
				"\n" +
				"\n" +
				"table {\n" +
				"    width: 100%;\n" +
				"    border-collapse: collapse;\n" +
				"    margin: 0 auto;\n" +
				"}\n" +
				"\n" +
				".td1 {\n" +
				"\twidth:50%;\n" +
				"    padding: 10px 10px 10px 5px;\n" +
				"}\n" +
				"\n" +
				".td2 {\n" +
				"\twidth:50%;\n" +
				"    padding: 0px 0px 10px 20px;\n" +
				"}\n" +
				"</style>\n" +
				"\n" +
				"</head>\n" +
				"<body>\n" +
				"\n" +
				"\t<div class=\"navbar\">\n" +
				"\t\t<a th:href=\"@{/}\">Logout</a>\n" +
				"\t\t<a th:href=\"@{/slot/available}\">Available Slots</a>\n" +
				"\t\t<a th:href=\"@{/calendar}\">Agenda</a>\n" +
				"\t  \t<a th:href=\"@{#}\" style=\"float: left;background: #ddd;color: black;font-weight:bold;\">Spring Boot Application</a>\n" +
				"\t</div>\n" +
				"\n" +
				"\t<div style=\"width: 50%;margin: 0 auto;margin-top:120px;padding:20px;border: 1px solid gray;\">\n" +
				"\t\t<h2 style=\"text-align:center;padding-bottom:20px;\" th:text=\"${title}\"></h2>\n" +
				"\n" +
				"\t\t<table>\n" +
				"\t\t\t<tbody>\n" +
				"\t\t\t\t<tr>\n" +
				"\t\t\t\t\t<td class=\"td1\" style=\"font-weight:bold;text-align:center;\">Available Time Slots</td>\n" +
				"\t                <td class=\"td2\" style=\"font-weight:bold;text-align:center;\">Duration (min)</td>\n" +
				"\t\t\t\t</tr>\n" +
				"\t\t\t\t<tr th:each=\"calendarObj: ${calendarObjs}\">\n" +
				"\t                <td th:text=\"${calendarObj.startEnd}\" class=\"td1\" style=\"text-align:center;\"></td>\n" +
				"\t                <td th:text=${calendarObj.duration} class=\"td2\" style=\"text-align:center;\"></td>\n" +
				"\t            </tr>\n" +
				"\t\t\t</tbody>\n" +
				"\t\t</table>\n" +
				"\t</div>\n" +
				"\n" +
				"</body>\n" +
				"</html>\n";
	}


	public Set<Event> getEvents() throws IOException {
		return this.events;
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String accessDenied(Model model) {

		model.addAttribute("message", "Not authorised.");
		return "login";

	}

	private String getLoginPage() {
		return "<!DOCTYPE html>\n" +
				"<html>\n" +
				"<head>\n" +
				"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
				"<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
				"<style>\n" +
				"body {\n" +
				"  font-family: Arial, Helvetica, sans-serif;\n" +
				"}\n" +
				"\n" +
				"* {\n" +
				"  box-sizing: border-box;\n" +
				"}\n" +
				"\n" +
				"\n" +
				".container {\n" +
				"  width: 50%;\n" +
				"  border-radius: 5px;\n" +
				"  background-color: #f2f2f2;\n" +
				"  padding: 20px 0 30px 0;\n" +
				"  position: fixed;\n" +
				"  top: 50%;\n" +
				"  left: 50%;\n" +
				"  transform: translate(-50%, -50%);\n" +
				"}\n" +
				"\n" +
				".btn {\n" +
				"  padding: 12px;\n" +
				"  border: none;\n" +
				"  border-radius: 4px;\n" +
				"  font-size: 17px;\n" +
				"  margin: 35%;\n" +
				"  line-height: 20px;\n" +
				"  text-decoration: none;\n" +
				"}\n" +
				"\n" +
				".google {\n" +
				"  background-color: #dd4b39;\n" +
				"  color: white;\n" +
				"}\n" +
				"\n" +
				"</style>\n" +
				"\n" +
				"</head>\n" +
				"<body>\n" +
				"\n" +
				"<h2 style=\"text-align:center; color:red\" th:utext=\"${message}\"></h2>\n" +
				"\n" +
				"<div class=\"container\">\n" +
				"  <h2 style=\"text-align:center\">Spring Boot Application : Calendar Event</h2>\n" +
				"\n" +
				"  <div>\n" +
				"    <a th:href=\"{@{/calendar}}\" class=\"google btn\">\n" +
				"        <i class=\"fa fa-google fa-fw\"></i> Login with Google\n" +
				"    </a>\n" +
				"  </div>\n" +
				"</div>\n" +
				"\n" +
				"\n" +
				"</body>\n" +
				"</html>";
	}


	@RequestMapping(value = { "/", "/login", "/logout" }, method = RequestMethod.GET)
	public String login(Model model) {
		isAuthorised = false;
		return getLoginPage ();
	}


	private List<CalendarObj> getTodaysCalendarEventList(String calenderApiCode, String redirectURL) {
		try {
			com.google.api.services.calendar.model.Events eventList;

			LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
			LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
			LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);

			DateTime date1 = new DateTime(Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant()));
			DateTime date2 = new DateTime(Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant()));

			TokenResponse response = flow.newTokenRequest(calenderApiCode).setRedirectUri(redirectURL).execute();
			credential = flow.createAndStoreCredential(response, "userID");
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
			Events events = client.events();
			eventList = events.list("primary").setSingleEvents(true).setTimeMin(date1).setTimeMax(date2).setOrderBy("startTime").execute();

			List<Event> items = eventList.getItems();

			CalendarObj calendarObj;
			List<CalendarObj> calendarObjs = new ArrayList<CalendarObj>();

			for (Event event : items) {

				Date startDateTime = new Date(event.getStart().getDateTime().getValue());
				Date endDateTime = new Date(event.getEnd().getDateTime().getValue());


				long diffInMillies = endDateTime.getTime() - startDateTime.getTime();
				int diffmin = (int) (diffInMillies / (60 * 1000));

				calendarObj = new CalendarObj();

				if(event.getSummary() != null && event.getSummary().length() > 0) {
					calendarObj.setTitle(event.getSummary());
				} else {
					calendarObj.setTitle("No Title");
				}

				calendarObj.setStartHour(startDateTime.getHours());
				calendarObj.setStartMin(startDateTime.getMinutes());
				calendarObj.setEndHour(endDateTime.getHours());
				calendarObj.setEndMin(endDateTime.getMinutes());
				calendarObj.setDuration(diffmin);

				calendarObj.setStartEnd(sdf.format(startDateTime) + " - " + sdf.format(endDateTime));

				calendarObjs.add(calendarObj);
			}

			return calendarObjs;

		} catch (Exception e) {
			return new ArrayList<CalendarObj>();
		}
	}

	private List<CalendarObj> populateAvailableSlot(List<CalendarObj> calendarEventList) {
		int freeSlotStartHour = START_HOUR;
		int freeSlotStartMin = START_MIN;
		int freeSlotEndHour = END_HOUR;
		int freeSlotEndMin = END_MIN;

		CalendarObj freeCalEvent;
		CalendarObj thisCalEvent;
		CalendarObj nextCalEvent;
		List<CalendarObj> freeCalendarObjs = new ArrayList<CalendarObj>();

		for (int count = 0; count < calendarEventList.size() - 1; count++) {
			thisCalEvent = calendarEventList.get(count);

			int thisEventStartHour  = thisCalEvent.getStartHour();
			int thisEventStartMin  = thisCalEvent.getStartMin();
			int thisEventEndHour  = thisCalEvent.getEndHour();
			int thisEventEndMin  = thisCalEvent.getEndMin();

			if(count == 0) { //Check time slot of last schedule
				if((thisEventStartHour > START_HOUR) || (thisEventStartHour == START_HOUR && thisEventStartMin > START_MIN)) {
					freeSlotStartHour = START_HOUR;
					freeSlotStartMin = START_MIN;

					freeSlotEndHour = thisEventStartHour;
					freeSlotEndMin = thisEventStartMin;

					int diffmin = (thisEventStartHour - START_HOUR) * 60 + Math.abs(thisEventStartMin - START_MIN);

					freeCalEvent = new CalendarObj();

					freeCalEvent.setTitle("Free Slot 1");
					freeCalEvent.setStartHour(freeSlotStartHour);
					freeCalEvent.setStartMin(freeSlotStartMin);
					freeCalEvent.setEndHour(freeSlotEndHour);
					freeCalEvent.setEndMin(freeSlotEndMin);
					freeCalEvent.setDuration(diffmin);

					freeCalEvent.setStartEnd(freeSlotStartHour+":"+freeSlotStartMin+ " - " + freeSlotEndHour+":"+freeSlotEndMin);

					freeCalendarObjs.add(freeCalEvent);
				}
			}

			nextCalEvent = calendarEventList.get(count + 1);

			int nextEventStartHour  = nextCalEvent.getStartHour();
			int nextEventStartMin  = nextCalEvent.getStartMin();

			freeSlotStartHour = thisEventEndHour;
			freeSlotStartMin = thisEventEndMin;

			freeSlotEndHour = nextEventStartHour;
			freeSlotEndMin = nextEventStartMin;

			int diffmin = (nextEventStartHour - thisEventEndHour - 1) * 60 + Math.abs(nextEventStartMin + thisEventEndMin);
			if(nextEventStartMin == thisEventEndMin) {
				diffmin = (nextEventStartHour - thisEventEndHour) * 60;
			}

			if(diffmin > 0) {
				freeCalEvent = new CalendarObj();
				freeCalEvent.setTitle("Free Slot " + (count + 1));
				freeCalEvent.setStartHour(freeSlotStartHour);
				freeCalEvent.setStartMin(freeSlotStartMin);
				freeCalEvent.setEndHour(freeSlotEndHour);
				freeCalEvent.setEndMin(freeSlotEndMin);
				freeCalEvent.setDuration(diffmin);

				freeCalEvent.setStartEnd(freeSlotStartHour+":"+freeSlotStartMin+ " - " + freeSlotEndHour+":"+freeSlotEndMin);

				freeCalendarObjs.add(freeCalEvent);
			}
		}


		//Check time slot of last schedule
		thisCalEvent = calendarEventList.get(calendarEventList.size() - 1);

		int thisEventEndHour  = thisCalEvent.getEndHour();
		int thisEventEndMin  = thisCalEvent.getEndMin();

		if((thisEventEndHour < END_HOUR) || (thisEventEndHour == END_HOUR && thisEventEndMin < END_MIN)) {
			freeSlotStartHour = END_HOUR;
			freeSlotStartMin = END_MIN;

			freeSlotEndHour = thisEventEndHour;
			freeSlotEndMin = thisEventEndMin;

			int diffmin = (END_HOUR - thisEventEndHour - 1) * 60 + Math.abs(thisEventEndMin - END_MIN);

			freeCalEvent = new CalendarObj();

			freeCalEvent.setTitle("Free Slot " + (calendarEventList.size()));
			freeCalEvent.setStartHour(freeSlotStartHour);
			freeCalEvent.setStartMin(freeSlotStartMin);
			freeCalEvent.setEndHour(freeSlotEndHour);
			freeCalEvent.setEndMin(freeSlotEndMin);
			freeCalEvent.setDuration(diffmin);

			freeCalEvent.setStartEnd(freeSlotEndHour+":"+freeSlotEndMin +" - " +freeSlotStartHour+":"+freeSlotStartMin);

			freeCalendarObjs.add(freeCalEvent);
		}

		return freeCalendarObjs;
	}

}