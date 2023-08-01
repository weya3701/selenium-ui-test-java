package org.example;

public interface WebAutomationTool {
  static final String SUCCESSFUL = "執行成功";
  static final String FAILED = "執行失敗";

  public void open_website(Step step);

  public void find_element_and_click(Step step);

  public void switch_tab(Step step);

  public void open_new_tab(Step step);

  public void find_element_and_click_without_wait(Step step);

  public void find_element_and_sendkey(Step step);

  public void find_element_and_sendkey_from_store(Step step);

  public void find_element_and_sendkey_by_js(Step step);

  public void switch_frame(Step step);

  public void get_value_to_store(Step step);

  public void get_regex_value_to_store(Step step);

  public void validation_count(Step step);

  public void scroll_element_intoview(Step step);

  public void validation(Step step);

  public void find_element_and_hover(Step step);
}
