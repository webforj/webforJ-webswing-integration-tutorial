package com.webforj.webswing.demo.views;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.Route;
import com.webforj.webswing.demo.components.CustomerForm;
import com.webforj.component.webswing.WebswingConnector;
import org.springframework.beans.factory.annotation.Value;

@Route("/")
public class CustomerTableView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public CustomerTableView(@Value("${webswing.connector.url}") String webswingUrl) {
    WebswingConnector connector = new WebswingConnector(webswingUrl);
    connector.setSize("100vw", "100vh");

    connector.onAction(event -> {
      switch (event.getActionName()) {
        case "select-customer":
          event.getActionData().ifPresent(data -> {
            JsonObject customer = JsonParser.parseString(data).getAsJsonObject();
            CustomerForm dialog = new CustomerForm(customer);
            self.add(dialog);
            dialog.onSave(() -> {
              Gson gson = new Gson();
              connector.performAction("update-customer", gson.toJson(customer));
            });
          });
          break;
        default:
          break;
      }
    });

    self.add(connector);
  }
}
