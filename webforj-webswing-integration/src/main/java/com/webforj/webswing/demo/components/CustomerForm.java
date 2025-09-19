package com.webforj.webswing.demo.components;

import com.google.gson.JsonObject;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.dialog.Dialog;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.H3;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class CustomerForm extends Composite<Dialog> {
  private Dialog self = getBoundComponent();
  private TextField nameField = new TextField("Name");
  private TextField companyField = new TextField("Company");
  private TextField emailField = new TextField("Email");
  private FlexLayout form = FlexLayout.create(nameField, companyField, emailField).vertical().build();
  private Button saveButton = new Button("Save", ButtonTheme.PRIMARY);
  private Button cancelButton = new Button("Cancel");
  private FlexLayout buttons = FlexLayout.create(saveButton, cancelButton).horizontal().build();
  private Runnable action = () -> {
  };

  public CustomerForm(JsonObject customer) {
    self.setMaxWidth("400px");
    self.setCloseable(false);
    self.add(form);
    self.addToHeader(new H3("Edit Customer"));
    self.addToFooter(buttons);

    nameField.setValue(customer.get("name").getAsString());
    companyField.setValue(customer.get("company").getAsString());
    emailField.setValue(customer.get("email").getAsString());

    cancelButton.onClick(event -> self.close());
    saveButton.onClick(event -> {
      customer.addProperty("name", nameField.getValue());
      customer.addProperty("company", companyField.getValue());
      customer.addProperty("email", emailField.getValue());

      action.run();
      self.destroy();
    });
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    self.open();
  }

  public void onSave(Runnable action) {
    this.action = action;
  }
}
