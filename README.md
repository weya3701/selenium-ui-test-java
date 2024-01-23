## Selenium Task Modules

* * *

### Selenium 自動測試系統運作前置作業：

* 撰寫測試案例步驟。

    * 測案步驟描述檔為 task.yaml

    * 每個步驟需有六個參數(element_name, desc, interval, url, by, key)

        * element\_name: 網頁元素，可以是class ID xpath link_text...
        * desc: 測案步驟描述。
        * module: 測案步驟對應使用模組，控制行為用。
        * url: 網頁網址。
        * by: 元素導航方式 (xpath, class ID link\_text...)。
        * key: 有輸入框時使用。

    * 指定元素導航方式，使用參數by
        
        * css: 使用CSS Seelctor
        * xpath: 使用XPATH
        * link_text: 使用連結文字

* * *

### Selenium Task Runner支援的動作類型

* open_website: 開啟指定網頁

    * 使用參數：url, desc, interval

* find\_element\_and\_click: 找到特定元素並按下

    * 使用參數： element\_name, desc, by, interval

* find\_element\_and\_sendkey: 找到搜尋框並輸入指定字串

    * 使用參數： element\_name, desc, by, key, interval

* find\_element\_and\_hover: 找到元素並使出Mouse Hover動作

    * 使用參數： element\_name, desc, by, interval

* find\_element\_and\_click\_without\_wait: 與find\_element\_and\_clidk行為類似，主要用於Mouse hover後的找元素並按下

    * 使用參數： element\_name, desc, by, interval

* scroll\_element\_intoview: 捲動視窗使指定元素進入可視範圍

    * 使用參數： element\_name, desc, interval

* get\_screenshot: 擷取當下畫面

    * 使用參數： file_name

* webdriver\_close: 關閉瀏覽器
    * 使用參數： None

* open\_new\_tab: 開啟新頁籤
    * 使用參數： None

* get\_value\_to\_store: 取得元素值並儲存在暫存空間
    * 使用參數： store\_key, element\_name

* get\_regex\_value\_to\_store: 透過正規表示式取得資料並儲存在暫存空間
    * 使用參數： pattern, store_key

* find\_element\_and\_sendkey\_from\_store: 找到搜尋框用暫存元素值作為查詢字詞
    * 使用參數： element\_name, desc, interval, by, store\_key 

* find\_element\_and\_sendkey\_by\_js: 利用javascript將值填入欄位
    * 使用參數：key, element_name 

* validation: 驗證預期資料是否符合
    * 使用參數： elementName, result
    * 參數說明：
         * elementName: 預期資料elementName, 主要使用python正規表示式。
         * result: 預期結果資料 

* validation_count: 驗證資料數量是否一致或有重複
    * 使用參數： pattern

* switch\_tab: 切換頁籤
    * 使用參數： tab

* switch\_frame: 切換iFrame
    * 使用參數： frame
    * 參數說明：
        * iFrame名稱

* set\_shadow\_root: 設定shadowRoot節點
    * 使用參數： elementName

* find_shadow_root_element_and_click
    * 使用參數： elementName

* find_shadow_root_element_and_sendkey
    * 使用參數： elementName, key

### 注意事項：

* 必須使用xpath方式進行元素導航的模組

    * scroll\_element\_intoview
    * get\_value\_to\_store
    * find\_element\_and\_sendkey\_by\_js


* 必須使用Regex正規表示式的模組

    * get\_value\_to\_store
    * validation 

* * *

Author: Allen Yang

Date: 2023/06/02