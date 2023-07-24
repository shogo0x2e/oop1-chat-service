package com.shogo0x2e.oop1.chatService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.annotation.PostConstruct;

@Controller
public class IndexController {

	private LocalDateTime now;

	// Unity の Awake() みたいなもの
	// Bean (モジュール的な) が初期化された後に呼ばれる
	@PostConstruct
	public void init() {
		now = LocalDateTime.now();
	}

	@GetMapping("/")
	public String index(Model model) {

		var dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		now = LocalDateTime.now();

		model.addAttribute("time", dtf.format(now));
		return "index";
	}
}

/**
 * 開発メモ:
 * 	http://bus.shibaura-it.ac.jp/db/bus_data.json から fetch
 *  json 抽出
 *  html に出力
 */
