package com.webforj.swingapp;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import org.webswing.toolkit.api.WebswingApi;
import org.webswing.toolkit.api.WebswingUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Application {
  private WebswingApi api;
  private boolean isWebswing;
  private List<Customer> customers;
  private DefaultTableModel model;
  private JTable table;
  private Gson gson;

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Application().start());
  }

  private void start() {
    initWebswing();
    initData();
    
    JFrame frame = createFrame();
    frame.add(createTablePanel(), BorderLayout.CENTER);
    frame.setVisible(true);
  }

  private void initWebswing() {
    api = WebswingUtil.getWebswingApi();
    isWebswing = api != null;

    if (isWebswing) {
      gson = new Gson();
      setupWebswingListeners();
    }
  }

  private void initData() {
    customers = new ArrayList<>();
    Faker faker = new Faker();

    for (int i = 1; i <= 10; i++) {
      customers.add(new Customer(i,
        faker.name().fullName(),
        faker.company().name(),
        faker.internet().emailAddress()));
    }
  }

  private JFrame createFrame() {
    JFrame frame = new JFrame("Customer Table");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 400);
    frame.setLocationRelativeTo(null);

    return frame;
  }

  private JScrollPane createTablePanel() {
    createTable();
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    return scrollPane;
  }

  private void createTable() {
    String[] columnNames = { "Name", "Company", "Email" };
    model = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    table = new JTable(model);
    table.setRowHeight(30);
    table.setFocusable(false);
    table.setRowSelectionAllowed(true);

    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          handleDoubleClick(e);
        }
      }
    });

    refresh();
  }

  private void handleDoubleClick(MouseEvent e) {
    int row = table.rowAtPoint(e.getPoint());
    if (row >= 0 && row < customers.size()) {
      Customer customer = customers.get(row);

      if (isWebswing) {
        api.sendActionEvent("select-customer", gson.toJson(customer), null);
      } else {
        showEditDialog(customer);
      }
    }
  }

  private void setupWebswingListeners() {
    api.addBrowserActionListener(event -> {
      if ("update-customer".equals(event.getActionName())) {
        Customer updated = gson.fromJson(event.getData(), Customer.class);
        updateCustomer(updated);
      }
    });
  }

  private void updateCustomer(Customer updated) {
    for (int i = 0; i < customers.size(); i++) {
      if (customers.get(i).getId() == updated.getId()) {
        customers.set(i, updated);
        refresh();
        
        table.setRowSelectionInterval(i, i);
        break;
      }
    }
  }

  private void refresh() {
    model.setRowCount(0);
    for (Customer customer : customers) {
      model.addRow(new Object[] {
        customer.getName(),
        customer.getCompany(),
        customer.getEmail()
      });
    }
  }

  private void showEditDialog(Customer customer) {
    JTextField nameField = new JTextField(customer.getName());
    JTextField companyField = new JTextField(customer.getCompany());
    JTextField emailField = new JTextField(customer.getEmail());

    Object[] fields = {
        "Name:", nameField,
        "Company:", companyField,
        "Email:", emailField
    };

    int result = JOptionPane.showConfirmDialog(null, fields, "Edit Customer",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      customer.setName(nameField.getText());
      customer.setCompany(companyField.getText());
      customer.setEmail(emailField.getText());
      updateCustomer(customer);
    }
  }
}