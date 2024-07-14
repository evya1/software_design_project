# Things to do when creating a new Controller class

## 1. Implement `ClientDependent`
All controller classes must implement the `ClientDependent` interface. This ensures that the controller can manage its dependency on the client's state effectively.

## 2. Register and Unregister with EventBus
Controllers must manage their connection to the server via the EventBus:

- **Register**: Use `EventBus.getDefault().Register(this);` when the controller needs to initiate communication with the server. This registration process involves waiting for server responses, so ensure the controller is capable of handling incoming data asynchronously.
    - **Subscribe**: Implement a `@Subscribe` method to process the data received from the server.

- **Unregister**: Use `EventBus.getDefault().Unregister(this);` when the controller no longer needs to receive updates from the server. This is crucial when the controller transitions activities or when the user navigates to another window, eliminating unnecessary data processing and network traffic.

## 3. Handling Messages
When using the `Message` object to communicate within the system, adhere to the following structure:
- **Main Message**: The `message` field should clearly define the primary purpose of the communication.
- **Data Message**: The `data` field should specify additional details that support the main message.

### Examples:
1. **Movie Request Specific**:
    - `message`: "Movie Request"
    - `data`: "Get specific movie"
2. **Movie Request General**:
    - `message`: "Movie Request"
    - `data`: "Show all movies"

Use these guidelines to ensure that messages are clear and actionable, enhancing the efficiency of data exchange across different components of the application.
