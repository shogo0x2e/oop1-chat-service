package com.shogo0x2e.oop1.chatService;

// 日付
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Spring のモジュールたち
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.annotation.PostConstruct;

// ネットたち
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class IndexController {

	private final String busUrlString = "http://bus.shibaura-it.ac.jp/db/bus_data.json";

	private JsonSearchingEngine jsonSearchingEngine;
	private LocalDateTime now;

	public IndexController() {

		try {
			// busUrlString から時刻表を取得する
			jsonSearchingEngine = new JsonSearchingEngine(
				this.getBusData()
			);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Unity の Awake() みたいなもの
	// Bean (モジュール的な) が初期化された後に呼ばれる
	@PostConstruct
	public void init() {
		this.now = LocalDateTime.now();
	}

	// ルーティング
	@GetMapping("/")
	public String index(Model model) {

		var dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		this.now = LocalDateTime.now();

		model.addAttribute("time", dtf.format(now));
		model.addAttribute("nextBusTime", jsonSearchingEngine.getNextBus(this.now.getDayOfMonth(), this.now.getHour()));
		return "index";
	}



	//----------------------------------------
	// 								ヘルパー関数
	//----------------------------------------



	private JsonObject getBusData() throws Exception {

		// busUrlString から時刻表を取得する
		URL url = new URL(busUrlString);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		int responseCode = conn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}

			br.close();

			// gson つかって json パース
			String result = response.toString();
			JsonElement element = JsonParser.parseString(result);

			return element.getAsJsonObject();
		}
		else {
			throw new RuntimeException("GET request not worked");
		}
	}
	
}

/**
 * 開発メモ:
 * 	~~ http://bus.shibaura-it.ac.jp/db/bus_data.json から fetch ~~
 *  ~~ json 抽出 ==
 *  html に出力
 */
