package com.shogo0x2e.oop1.chatService;

import java.time.LocalDateTime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonSearchingEngine {
  
	private JsonObject busData;

  public JsonSearchingEngine(JsonObject busData) {
    this.busData = busData;
  }

  public String getNextBus(int day, int hour) {

    if (hour < 7 || hour > 22) {
      return "運行時間外";
    }

    // ts_id を取得する
    String tsId = "";

		JsonElement busDataElement = busData.get("calendar");
		JsonElement busDataArray = busDataElement.getAsJsonArray().get(0);  // 最初に今月がくる
		JsonElement busStatus = busDataArray.getAsJsonObject().get("list");
    JsonElement todayElement = busStatus.getAsJsonArray().get(day);
    JsonElement todayId = todayElement.getAsJsonObject().get("ts_id");

    tsId = todayId.getAsString();

    // timesheet から ts_id に対応する時刻表を取得する
    String busTimes = "";

		JsonElement timeSheetArray = busData.get("timesheet");

    int matchTsIdIndex = 0;
    JsonElement timeSheetElement = timeSheetArray.getAsJsonArray().get(0);

    while (true) {
      JsonElement timeSheetId = timeSheetElement.getAsJsonObject().get("ts_id");
      if (timeSheetId.getAsString().equals(tsId)) {
        break;
      }
      matchTsIdIndex++;
      timeSheetElement = timeSheetArray.getAsJsonArray().get(matchTsIdIndex);
    }

    JsonElement timeList = timeSheetElement.getAsJsonObject().get("list");
    JsonElement currentHourTable = timeList.getAsJsonArray().get(hour - 7);
    JsonElement busTime = currentHourTable.getAsJsonObject().get("bus_left");
    JsonElement busLeftTime = busTime.getAsJsonObject().get("num1");

    busTimes = busLeftTime.getAsString();

    if (busTimes.equals("")) {
      busTimes = busTime.getAsJsonObject().get("memo1").getAsString();
    }

    return busTimes;
  }
}
