# Payment Service - Metro Transportation System

The Payment Service is a core component of the metro system that handles all financial transactions, including fare payments, penalty management, and metro card operations. It processes regular transactions between stations (calculating fares based on distance with optional discounts for card payments), enforces penalties for overstays (charging 50% of base fare when journeys exceed 90 minutes), and integrates with User and Metro services to validate travel histories, update balances, and maintain payment records. The service ensures secure transactions through status tracking (SUCCESS/PENDING/FAILED), supports multiple payment modes (card/cash), and maintains audit trails of all financial activities while enforcing business rules like balance checks and penalty calculations.
## Contributors

* [@Sonupatel](https://github.com/Sonupatel15)
* [@Harsha](https://github.com/harsha188-codes)

## DB Schema

**Transaction Table**

![transaction table](https://github.com/user-attachments/assets/3a584dd5-2d07-4a27-a357-cd4f3df7650d)

**Penalty Table**

![penalty table](https://github.com/user-attachments/assets/9e74a740-803e-4694-866a-626a6a8f8cc6)

## Transaction Management

### Create Transaction

* **Method:** POST
* **Endpoint:** /api/transactions
* **Description:** Processes a metro journey payment
* **Request Body:**

    ```json
    {
      "userId": "3320debf-15a4-4327-81e6-62753969fe39",
      "fromStationId": 3,
      "toStationId": 4,
      "modeOfPayment": "CARD",
      "status": "PENDING"
    }
    ```

* **Response:**

    ![Create Transaction Response](https://github.com/user-attachments/assets/01b26eeb-8b10-4765-b740-a363439a48be)

## Penalty Management

### Create Penalty

* **Method:** POST
* **Endpoint:** /penalties
* **Description:** Creates a new penalty record for overdue journeys
* **Body:**

    ```json
    {
      "travelId": "ef9ff112-d701-4940-babc-5c3c0ad432d1",
      "timingId": "308d79a1-4bdf-469d-ac3c-ef420a9cabc4"
    }
    ```

* **Response:**

    ![Create Penalty Response](https://github.com/user-attachments/assets/0d6ce79f-2cbf-43aa-ab40-c5a4b25f712b)
