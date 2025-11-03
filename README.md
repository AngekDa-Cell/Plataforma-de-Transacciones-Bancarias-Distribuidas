# Plataforma de Transacciones Bancarias Distribuidas
## Distributed Banking Transaction Platform

A Java-based distributed banking transaction platform that provides core banking operations with thread-safe transaction management and support for distributed operations.

## Features

- **Customer Management**: Create and manage bank customers
- **Account Management**: Create, view, and manage bank accounts
- **Transaction Operations**:
  - Deposits
  - Withdrawals
  - Inter-account transfers
- **Thread-Safe Operations**: All banking operations are thread-safe with proper locking mechanisms
- **Distributed Transaction Support**: Deadlock-free transfer operations using consistent lock ordering
- **Transaction History**: Complete audit trail of all banking operations
- **Account Status Management**: Support for active, suspended, and closed accounts

## Project Structure

```
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── bank/
│   │               ├── model/          # Domain models
│   │               │   ├── Account.java
│   │               │   ├── AccountStatus.java
│   │               │   ├── Customer.java
│   │               │   ├── Transaction.java
│   │               │   ├── TransactionStatus.java
│   │               │   └── TransactionType.java
│   │               ├── service/        # Business logic
│   │               │   └── BankingService.java
│   │               ├── exception/      # Custom exceptions
│   │               │   ├── AccountNotFoundException.java
│   │               │   ├── InsufficientFundsException.java
│   │               │   └── TransactionException.java
│   │               ├── util/           # Utilities
│   │               │   └── IdGenerator.java
│   │               └── BankApplication.java  # Main application
│   └── test/
│       └── java/
│           └── com/
│               └── bank/
│                   ├── model/          # Unit tests for models
│                   └── service/        # Unit tests for services
└── pom.xml                             # Maven configuration
```

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Building the Project

```bash
# Clone the repository
git clone https://github.com/AngekDa-Cell/Plataforma-de-Transacciones-Bancarias-Distribuidas.git
cd Plataforma-de-Transacciones-Bancarias-Distribuidas

# Compile the project
mvn clean compile

# Run tests
mvn test

# Package the application
mvn package
```

## Running the Application

```bash
# Run the demo application
mvn exec:java -Dexec.mainClass="com.bank.BankApplication"
```

## Usage Examples

### Creating Customers and Accounts

```java
BankingService bankingService = new BankingService();

// Create a customer
Customer customer = bankingService.createCustomer(
    "CUST-001",
    "John Doe",
    "john.doe@example.com",
    "+1-555-0001"
);

// Create an account
Account account = bankingService.createAccount(
    "ACC-001",
    customer.getCustomerId(),
    new BigDecimal("1000.00")
);
```

### Performing Transactions

```java
// Deposit money
Transaction deposit = bankingService.deposit(
    "ACC-001",
    new BigDecimal("250.00")
);

// Withdraw money
Transaction withdrawal = bankingService.withdraw(
    "ACC-001",
    new BigDecimal("100.00")
);

// Transfer between accounts
Transaction transfer = bankingService.transfer(
    "ACC-001",
    "ACC-002",
    new BigDecimal("300.00")
);
```

### Retrieving Information

```java
// Get account balance
BigDecimal balance = bankingService.getBalance("ACC-001");

// Get transaction history
List<Transaction> transactions = bankingService.getAccountTransactions("ACC-001");

// Get customer accounts
List<Account> accounts = bankingService.getCustomerAccounts("CUST-001");
```

## Key Features

### Thread Safety

All banking operations are protected with proper synchronization mechanisms:
- Account-level locking for deposits and withdrawals
- Consistent lock ordering for transfers to prevent deadlocks
- Thread-safe concurrent data structures

### Error Handling

The platform includes comprehensive exception handling:
- `InsufficientFundsException`: Thrown when account balance is insufficient
- `AccountNotFoundException`: Thrown when an account or customer is not found
- `TransactionException`: Thrown for general transaction errors

### Transaction Management

All operations create transaction records with:
- Unique transaction IDs
- Timestamps
- Transaction status (PENDING, COMPLETED, FAILED, CANCELLED)
- Transaction type (DEPOSIT, WITHDRAWAL, TRANSFER)
- Descriptions

## Testing

The project includes comprehensive unit tests:

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report
```

Test coverage includes:
- Model validation tests
- Banking service operation tests
- Exception handling tests
- Concurrent operation tests

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is open source and available under the MIT License.

## Authors

- AngekDa-Cell

## Acknowledgments

Built with:
- Java 11
- JUnit 5 for testing
- Maven for build management