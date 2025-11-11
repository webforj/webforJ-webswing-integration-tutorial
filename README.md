# webforJ Webswing Integration Tutorial

This tutorial demonstrates how to integrate existing Java Swing applications into webforJ web applications using the `WebswingConnector` component.

## Overview

The project consists of two applications:

1. **Swing Application** (`webforj-swing-app`) - A traditional Java Swing customer management table
2. **webforJ Application** (`webforj-webswing-integration`) - A web app that embeds the Swing app

This architecture enables progressive modernization: embed your existing Swing apps today, then gradually replace them with webforJ components over time.

## Prerequisites

- Java 21+
- Maven 3.9+
- Webswing Server (for running the Swing app in web mode)

## Quick Start

### 1. Build the Swing Application

```bash
cd custom-table-app
mvn clean package
# Creates custom-table-app.jar
```

The Swing app automatically detects if it's running under Webswing and adapts its behavior:

- **Standalone mode**: Shows standard Swing dialogs for editing
- **Webswing mode**: Communicates with the webforJ wrapper via events

### 2. Deploy to Webswing Server

Use your webswing admin console at http://localhost:8080/admin to add and configure your swing app. 

In the admin console, configure:

- **Application Name**: becomes part of the URL path (e.g., `custom-table-app` → `http://localhost:8080/custom-table-app/`)
- **Main Class**: the entry point of your Swing app
- **Classpath**: path to your app JAR and dependencies
- **JVM Arguments**: memory settings, system properties, and other JVM options
- **Home Directory**: working directory for the app

:::info
Make sure to move your previously built custom-table-app.jar to the folder you set as your classpath.
:::

After configuration, your Swing app will be accessible at http://localhost:8080/custom-table-app/

### 3. Run the webforJ Application

```bash
cd webforj-webswing-integration
mvn spring-boot:run
```

Open [http://localhost:8090](http://localhost:8090) to see the integrated application.

## How It Works

### WebswingConnector Component

The `WebswingConnector` embeds Webswing-hosted applications directly in your webforJ app:

```java
@Route("/")
public class CustomerTableView extends Composite<FlexLayout> {

  public CustomerTableView(@Value("${webswing.connector.url}") String webswingUrl) {
    WebswingConnector connector = new WebswingConnector(webswingUrl);
    connector.setSize("100vw", "100vh");

    // Handle events from Swing app
    connector.onAction(event -> {
      if ("select-customer".equals(event.getActionName())) {
        // Show webforJ dialog for editing
      }
    });

    self.add(connector);
  }
}
```

### Bidirectional Communication

**Swing → webforJ**: The Swing app sends events using Webswing API:
```java
if (isWebswing) {
  api.sendActionEvent("select-customer", gson.toJson(customer), null);
}
```

**webforJ → Swing**: The webforJ app sends commands back:
```java
connector.performAction("update-customer", gson.toJson(customer));
```

## Configuration

Configure the Webswing URL in `application.properties`:

```properties
# Webswing connector configuration
webswing.connector.url=http://localhost:8080/custom-table-app/

# Server configuration
server.port=8090
```

## Architecture Benefits

This integration pattern provides:

- **Zero modification deployment** - Run existing Swing apps in the browser immediately
- **Progressive modernization** - Replace Swing components with webforJ gradually
- **Hybrid applications** - Combine web UI with specialized desktop functionality
- **Investment protection** - Preserve years of business logic development

## Learn More

- [webforJ Official Documentation](https://docs.webforj.com/docs/integrations/webswing/overview)
- [Webswing Official Site](https://www.webswing.org)
- [Spring Boot with webforJ](https://docs.webforj.com/docs/integrations/spring/overview)
