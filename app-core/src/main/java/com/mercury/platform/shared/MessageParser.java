package com.mercury.platform.shared;

import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessageParser {
    private final static String poeTradeStashTabPattern = "^(.*\\s)?(.+): (.+ to buy your\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash tab \"(.*)\"; position: left (\\d+), top (\\d+)\\)\\s*?(.*))$";
    private final static String poeTradePattern = "^(.*\\s)?(.+): (.+ to buy your\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.*?))$";
    private final static String poeAppPattern = "^(.*\\s)?(.+): (\\s*?wtb\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash\\s+?\"(.*?)\";\\s+?left\\s+?(\\d+?),\\s+?top\\s+(\\d+?)\\)\\s*?(.*))$";
    private final static String poeAppBulkCurrenciesPattern = "^(.*\\s)?(.+): (\\s*?wtb\\s+?(.+?)(\\s+?listed for\\s+?([\\d\\.]+?)\\s+?(.+))?\\s+?in\\s+?(.+?)\\s+?\\(stash\\s+?\"(.*?)\";\\s+?left\\s+?(\\d+?),\\s+?top\\s+(\\d+?)\\)\\s*?(.*))$";
    private final static String poeCurrencyPattern = "^(.*\\s)?(.+): (.+ to buy your (\\d+(\\.\\d+)?)? (.+) for my (\\d+(\\.\\d+)?)? (.+) in (.*?)\\.\\s*(.*))$";
    private final static String poeMapLiveRegex = "^(.*\\s)?(.+): (I'd like to exchange my (.+:\\s\\([\\s\\S,]+) for your (.+:\\s\\([\\S,\\s]+) in\\s+?(.+?)\\.)";
    private final static String poeTradeKoreanRegex = "^(.*\\s)?(.+): 안녕하세요, 강탈\\(보관함 탭 \"(.*)\", 위치: 왼쪽 (\\d+), 상단 (\\d+)\\)에\\s+?([\\d\\.]*)\\s*(.*)\\s*\\(으\\)로 올려놓은\\s*(.*)s*(.*)\\(을\\)를\\s구매하고 싶습니다";
    private final static String strTradeStashTabKorPa = "^(.*\\s)?(.+): (안녕하세요, (.+?)\\s*\\(보관함 탭 \"(.*)\", 위치: 왼쪽 (\\d+), 상단 (\\d+)\\)에\\s+?([\\d\\.]*)\\s*(.*)\\s*\\(으\\)로 올려놓은\\s*(.*)을\\(를\\) 구매하고 싶습니다\\s*(.*))$";
    private final static String strBulkCurrenciesKorP = "^(.*\\s)?(.+): (안녕하세요, (.+?)\\s*에\\s+?올려놓은\\s*(.*)을\\(를\\) 제 ([\\d\\.]*)\\s*(.*)\\s*\\(으\\)로 구매하고 싶습니다\\s*(.*))$";
    private Pattern poeAppItemPattern;
    private Pattern poeTradeStashItemPattern;
    private Pattern poeTradeItemPattern;
    private Pattern poeTradeCurrencyPattern;
    private Pattern poeMapLivePattern;
    private Pattern poeTradeKoreanPattern;

    public MessageParser() {
        this.poeAppItemPattern = Pattern.compile(poeAppPattern);
        this.poeTradeStashItemPattern = Pattern.compile(poeTradeStashTabPattern);
        this.poeTradeItemPattern = Pattern.compile(poeTradePattern);
        this.poeTradeCurrencyPattern = Pattern.compile(poeCurrencyPattern);
        this.poeMapLivePattern = Pattern.compile(poeMapLiveRegex);
        this.poeTradeKoreanPattern = Pattern.compile(poeTradeKoreanRegex);
    }

    public NotificationDescriptor parse(String fullMessage) {
        Matcher poeAppItemMatcher = poeAppItemPattern.matcher(fullMessage);
        if (poeAppItemMatcher.find()) {
            ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
            tradeNotification.setWhisperNickname(poeAppItemMatcher.group(2));
            tradeNotification.setSourceString(poeAppItemMatcher.group(3));
            tradeNotification.setItemName(poeAppItemMatcher.group(4));
            if (poeAppItemMatcher.group(5) != null) {
                tradeNotification.setCurCount(Double.parseDouble(poeAppItemMatcher.group(6)));
                tradeNotification.setCurrency(poeAppItemMatcher.group(7));
            } else {
                tradeNotification.setCurCount(0d);
                tradeNotification.setCurrency("???");
            }
            tradeNotification.setLeague(poeAppItemMatcher.group(8));
            if (poeAppItemMatcher.group(9) != null) {
                tradeNotification.setTabName(poeAppItemMatcher.group(9));
                tradeNotification.setLeft(Integer.parseInt(poeAppItemMatcher.group(10)));
                tradeNotification.setTop(Integer.parseInt(poeAppItemMatcher.group(11)));
            }
            tradeNotification.setOffer(poeAppItemMatcher.group(12));
            tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
            return tradeNotification;
        }
        Matcher poeTradeStashItemMatcher = poeTradeStashItemPattern.matcher(fullMessage);
        if (poeTradeStashItemMatcher.find()) {
            ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
            tradeNotification.setWhisperNickname(poeTradeStashItemMatcher.group(2));
            tradeNotification.setSourceString(poeTradeStashItemMatcher.group(3));
            tradeNotification.setItemName(poeTradeStashItemMatcher.group(4));
            if (poeTradeStashItemMatcher.group(6) != null) {
                tradeNotification.setCurCount(Double.parseDouble(poeTradeStashItemMatcher.group(6)));
                tradeNotification.setCurrency(poeTradeStashItemMatcher.group(7));
            } else {
                tradeNotification.setCurCount(0d);
                tradeNotification.setCurrency("???");
            }
            tradeNotification.setLeague(poeTradeStashItemMatcher.group(8));
            tradeNotification.setTabName(poeTradeStashItemMatcher.group(9));
            tradeNotification.setLeft(Integer.parseInt(poeTradeStashItemMatcher.group(10)));
            tradeNotification.setTop(Integer.parseInt(poeTradeStashItemMatcher.group(11)));
            tradeNotification.setOffer(poeTradeStashItemMatcher.group(12));
            tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
            return tradeNotification;
        }
        Matcher poeTradeCurrencyMatcher = poeTradeCurrencyPattern.matcher(fullMessage);
        if (poeTradeCurrencyMatcher.find()) {
            CurrencyTradeNotificationDescriptor tradeNotification = new CurrencyTradeNotificationDescriptor();

            if (poeTradeCurrencyMatcher.group(6).contains("&") || poeTradeCurrencyMatcher.group(6)
                                                                                         .contains(",")) {  //todo this shit for bulk map
                String bulkItems = poeTradeCurrencyMatcher.group(4) + " " + poeTradeCurrencyMatcher.group(6);
                tradeNotification.setItems(Arrays.stream(StringUtils.split(bulkItems, ",&"))
                                                 .map(String::trim)
                                                 .collect(Collectors.toList()));
            } else {
                tradeNotification.setCurrForSaleCount(Double.parseDouble(poeTradeCurrencyMatcher.group(4)));
                tradeNotification.setCurrForSaleTitle(poeTradeCurrencyMatcher.group(6));
            }

            tradeNotification.setWhisperNickname(poeTradeCurrencyMatcher.group(2));
            tradeNotification.setSourceString(poeTradeCurrencyMatcher.group(3));
            tradeNotification.setCurCount(Double.parseDouble(poeTradeCurrencyMatcher.group(7)));
            tradeNotification.setCurrency(poeTradeCurrencyMatcher.group(9));
            tradeNotification.setLeague(poeTradeCurrencyMatcher.group(10));
            tradeNotification.setOffer(poeTradeCurrencyMatcher.group(11));
            tradeNotification.setType(NotificationType.INC_CURRENCY_MESSAGE);
            return tradeNotification;
        }
        Matcher poeTradeItemMatcher = poeTradeItemPattern.matcher(fullMessage);
        if (poeTradeItemMatcher.find()) {
            ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
            tradeNotification.setWhisperNickname(poeTradeItemMatcher.group(2));
            tradeNotification.setSourceString(poeTradeItemMatcher.group(3));
            tradeNotification.setItemName(poeTradeItemMatcher.group(4));
            if (poeTradeItemMatcher.group(5) != null) {
                tradeNotification.setCurCount(Double.parseDouble(poeTradeItemMatcher.group(6)));
                tradeNotification.setCurrency(poeTradeItemMatcher.group(7));
            } else {
                tradeNotification.setCurCount(0d);
                tradeNotification.setCurrency("???");
            }
            tradeNotification.setLeague(poeTradeItemMatcher.group(8));
            tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
            return tradeNotification;
        }
        Matcher poeTradeMapLiveMatcher = poeMapLivePattern.matcher(fullMessage);
        if (poeTradeMapLiveMatcher.find()) {
            ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
            tradeNotification.setWhisperNickname(poeTradeMapLiveMatcher.group(2));
            tradeNotification.setSourceString(poeTradeMapLiveMatcher.group(3));

            String itemsWanted = poeTradeMapLiveMatcher.group(5);
            if (itemsWanted.contains(",")) {
                String[] splitItemsWanted = itemsWanted.split(",");
                tradeNotification.setItemsWanted(Arrays.asList(splitItemsWanted));
            }
            tradeNotification.setItemName(itemsWanted);


            String itemsOffered = poeTradeMapLiveMatcher.group(4);
            if (itemsOffered.contains(",")) {
                String[] splitItemsWanted = itemsOffered.split(",");
                tradeNotification.setItemsOffered(Arrays.asList(splitItemsWanted));
            }
            tradeNotification.setOffer(itemsOffered);


            tradeNotification.setLeague(poeTradeMapLiveMatcher.group(6));
            tradeNotification.setCurCount(0d);
            tradeNotification.setCurrency(itemsWanted);
            tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
            return tradeNotification;
        }
        Matcher poeTradeKoreanMatcher = poeTradeKoreanPattern.matcher(fullMessage);
        if (poeTradeKoreanMatcher.find()) {
            ItemTradeNotificationDescriptor tradeNotification = new ItemTradeNotificationDescriptor();
            tradeNotification.setWhisperNickname(poeTradeKoreanMatcher.group(2));
            tradeNotification.setSourceString(poeTradeKoreanMatcher.group(3));
            tradeNotification.setItemName(poeTradeKoreanMatcher.group(8));
            if (poeTradeKoreanMatcher.group(6) != null) {
                tradeNotification.setCurCount(Double.parseDouble(poeTradeKoreanMatcher.group(6)));
                tradeNotification.setCurrency(poeTradeKoreanMatcher.group(7));
            } else {
                tradeNotification.setCurCount(0d);
                tradeNotification.setCurrency("???");
            }
            tradeNotification.setLeague(poeTradeKoreanMatcher.group(8));
            tradeNotification.setTabName(poeTradeKoreanMatcher.group(3));
            tradeNotification.setLeft(Integer.parseInt(poeTradeKoreanMatcher.group(4)));
            tradeNotification.setTop(Integer.parseInt(poeTradeKoreanMatcher.group(5)));
//            tradeNotification.setOffer(poeTradeKoreanMatcher.group(12));
            tradeNotification.setType(NotificationType.INC_ITEM_MESSAGE);
            return tradeNotification;
        }
        return null;
    }
}
