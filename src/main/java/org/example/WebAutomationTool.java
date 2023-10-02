package org.example;

public interface WebAutomationTool {

  public String open_website(Step step);

  public String find_element_and_click(Step step);

  public String switch_tab(Step step);

  public String open_new_tab(Step step);

  public String find_element_and_click_without_wait(Step step);

  public String find_element_and_sendkey(Step step);

  public String find_element_and_sendkey_from_store(Step step);

  public String find_element_and_sendkey_by_js(Step step);

  public String switch_frame(Step step);

  public String get_value_to_store(Step step);

  public String set_windows_size(Step step);

  public String get_regex_value_to_store(Step step);

  public String validation_count(Step step);

  public String scroll_element_intoview(Step step);

  public String validation(Step step);

  public String find_element_and_hover(Step step);

  public String find_element_and_click_with_wait(Step step);

  public String webdriver_run_script(Step step);

}