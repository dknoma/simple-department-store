package com.drew.department_store;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.drew.department_store.models.Customer;
import com.drew.department_store.models.Product;
import com.drew.department_store.models.User;
import com.drew.department_store.models.UserType;
import com.drew.department_store.service.CategoryFilter;
import com.drew.department_store.service.IdFilter;
import com.drew.department_store.service.NameFilter;
import com.drew.department_store.service.PriceFilter;
import com.drew.department_store.service.ProductFilter;
import com.drew.department_store.service.StoreInformation;
import com.drew.department_store.service.TrueFilter;
import com.drew.department_store.service.UserService;

public class DepartmentStoreApplication {

	private static UserType currentUserType = UserType.NONE;
	private static User currentUser = null;

	public static void main(String[] args) {
		StoreInformation.init();
		final String defaultOptions = """
			====================================================================
				Options
			====================================================================
				[register] [login] [exit]
						
			====================================================================
			""";
		final String normalOptions = """
			====================================================================
				Options
			====================================================================
				[logout] [exit]
						
			""";
		final String adminOptions = """
			====================================================================
				Admin Options
			====================================================================
				[all_products] [get_from_id=<product.id>] [get_from_name=<product.name>]
				[get_from_category=<product.category>] [total_spent=<optional_category>] [profits=<category>]
				
				possible categories: 'grocery', 'stationary', 'toiletry', 'vegetable
			====================================================================
			""";
		final String customerOptions = """
			====================================================================
				Customer Options
			====================================================================
				[filter=<price or category>] [clearFilter]
						
			====================================================================
			""";

//		SpringApplication.run(DepartmentStoreApplication.class, args);
		try (Scanner input = new Scanner(System.in)) {

			while (true) {
				System.out.printf("Please input an option --- user_type:'%s'\n", currentUserType);
				if (currentUserType == UserType.NONE) {
					System.out.println(defaultOptions);
				} else {
					System.out.println(normalOptions);

					if (currentUserType == UserType.ADMIN) {
						System.out.println(adminOptions);
					} else if (currentUserType == UserType.CUSTOMER) {
						System.out.println(customerOptions);
					}
				}

				String line = input.nextLine();
				// System.out.printf("input=%s\n", line);

				if (line.isEmpty()) {
					continue;
				}

				String[] split = line.split("=");
				String operation = split[0];
				String arguments = split.length > 1 ? split[1] : "";

				Operation op = Operation.fromName(operation);

				// System.out.printf("op='%s'\n", op.name());

				ErrorCode err = ErrorCode.OK;

				if (currentUserType == UserType.NONE) {
					if (op == Operation.LOGIN) {
						err = login(input);
					} else if (op == Operation.REGISTER) {
						err = register(input);
					}
				} else {

					if (op == Operation.INVALID) {
						System.out.printf("Invalid operation '%s', please try again.\n", op);
						continue;
					} else if (op == Operation.EXIT) {
						break;
					} else if (op == Operation.LOGOUT) {
						err = logout(input);
						// User
					} else if (op == Operation.FILTER) {
						err = filter(input, arguments);
					} else if (op == Operation.CLEAR_FILTER) {
						err = clearFilter(input);
						// Admin
					} else if (op == Operation.LIST_PRODUCTS) {
						err = listProducts(input);
					} else if (op == Operation.GET_FROM_ID) {
						err = getFromId(input, arguments);
					} else if (op == Operation.GET_FROM_NAME) {
						err = getFromName(input, arguments);
					} else if (op == Operation.GET_FROM_CATEGORY) {
						err = getFromCategory(input, arguments);
					// } else if (op == Operation.ADD_PRODUCT) {
					} else if (op == Operation.TOTAL_SPENT) {
						err = totalSpent(input, arguments);
					} else if (op == Operation.PROFITS) {
						err = profits(input, arguments);
					}
					System.out.println("Press enter to go back to menu.");
					String a = input.nextLine();
				}

				if (err == ErrorCode.EXIT) {
					break;
				}
			}
		} catch (InvalidInputException iie) {
			System.out.println("Input was invalid. Please try again.");
		}
	}

	/**
	 * Login Option
	 * @param input
	 * @return
	 * @throws InvalidInputException
	 */
	private static ErrorCode login(Scanner input) throws InvalidInputException {
		System.out.println("Please type your username");
		String username = input.nextLine();
		System.out.printf("\tusername=%s\n", username);

		while (!UserService.userExists(username)) {
			System.out.printf("User '%s' not found...\n", username);
			username = input.nextLine();
			System.out.printf("\tusername=%s\n", username);
		}

		System.out.println("Please enter your password");
		String password = input.nextLine();
		System.out.printf("\tpassword=%s\n", password);

		Optional<User> user = UserService.login(username, password);

		if (user.isEmpty()) {
			return ErrorCode.TRY_AGAIN;
		}

		System.out.printf("Welcome '%s'!\n", username);

		currentUser = user.get();
		currentUserType = UserType.valueOf(user.get()
											   .type()
											   .toUpperCase());

		return ErrorCode.OK;
	}

	/**
	 * Logout option
	 * @param input
	 * @return
	 */
	private static ErrorCode logout(Scanner input) {
		currentUser = null;
		currentUserType = UserType.NONE;
		return ErrorCode.OK;
	}

	/**
	 * Register Option
	 * @param input
	 * @return
	 */
	private static ErrorCode register(Scanner input) {
		System.out.println("Please choose your username");
		String username = input.nextLine();
		System.out.printf("\tusername=%s\n", username);
		System.out.println("Please enter your password");
		String password = input.nextLine();
		System.out.printf("\tpassword=%s\n", password);
		System.out.println("Please enter your email");
		String email = input.nextLine();
		System.out.printf("\temail=%s\n", email);

		Optional<Customer> customer = UserService.registerNewCustomer(username, email, password);

		if (customer.isEmpty()) {
			return ErrorCode.TRY_AGAIN;
		}

		currentUser = customer.get();
		currentUserType = UserType.valueOf(customer.get()
												   .type()
												   .toUpperCase());
		System.out.printf("customer=%s\n", customer);

		return ErrorCode.OK;
	}

	/**
	 * Filter option
	 * @param input
	 * @param args
	 * @return
	 * @throws InvalidInputException
	 */
	private static ErrorCode filter(Scanner input, String args) throws InvalidInputException {
		if (!args.isEmpty()) {
			if (args.equalsIgnoreCase("price")) {
				System.out.println("Filtering products by price...");
				ProductFilter filter = new PriceFilter();
				List<Product> products = StoreInformation.filteredProducts(filter);
				System.out.printf("products - low to high:\n%s\n", productsToString(products));
			} else if (args.equalsIgnoreCase("category")) {
				System.out.println("Please select a category: Grocery, Stationary, Toiletry, or Vegetable");
				String cat = input.nextLine();

				ProductFilter filter;
				if (!cat.equalsIgnoreCase("Grocery") &&
						!cat.equalsIgnoreCase("Stationary") &&
						!cat.equalsIgnoreCase("Toiletry") &&
						!cat.equalsIgnoreCase("Vegetable")
				) {
					cat = "";
					filter = new TrueFilter();
				} else {
					filter = CategoryFilter.of(cat);
				}

				List<Product> products = StoreInformation.filteredProducts(filter);
				System.out.printf("products:\n%s\n", productsToString(products));
			} else {
				String err = String.format("Filter '%s' is invalid, default to no filter\n", args);
				System.out.println(err);
				throw new InvalidInputException(err);
			}
		} else {
			System.out.println("No filter");
		}

		return ErrorCode.OK;
	}

	/**
	 * Clear Filter Option
	 * @param input
	 * @return
	 */
	private static ErrorCode clearFilter(Scanner input) {
		System.out.println("Cleared filter!");
		List<Product> products = StoreInformation.filteredProducts(new TrueFilter());
		System.out.printf("products:\n%s\n", productsToString(products));
		return ErrorCode.OK;
	}

	/**
	 * List Products Option
	 * @param input
	 * @return
	 */
	private static ErrorCode listProducts(Scanner input) {
		List<Product> products = StoreInformation.filteredProducts(new TrueFilter());
		System.out.printf("products:\n%s\n", productsToString(products));
		return ErrorCode.OK;
	}

	/**
	 * Get From Category Option
	 * @param input
	 * @param cat
	 * @return
	 * @throws InvalidInputException
	 */
	private static ErrorCode getFromCategory(Scanner input, String cat) throws InvalidInputException {
		if (!cat.equalsIgnoreCase("Grocery") &&
				!cat.equalsIgnoreCase("Stationary") &&
				!cat.equalsIgnoreCase("Toiletry") &&
				!cat.equalsIgnoreCase("Vegetable")
		) {
			throw new InvalidInputException(String.format("Invalid category '%s', please try again.", cat));
			// return ErrorCode.TRY_AGAIN;
		}
		List<Product> products = StoreInformation.filteredProducts(CategoryFilter.of(cat));
		System.out.printf("products for '%s':\n%s\n", cat.toLowerCase(), productsToString(products));
		return ErrorCode.OK;
	}

	/**
	 * Get From Id Option
	 * @param input
	 * @param args
	 * @return
	 */
	private static ErrorCode getFromId(Scanner input, String args) {
		try {
			IdFilter filter = IdFilter.of(Long.parseLong(args));
			List<Product> products = StoreInformation.filteredProducts(filter);
			System.out.printf("product by id[%s]:\n%s\n", args, productsToString(products));
		} catch (NumberFormatException nfe) {
			System.out.printf("Argument '%s' was not a number...\n", args);
			return ErrorCode.TRY_AGAIN;
		}
		return ErrorCode.OK;
	}

	/**
	 * Get From Name Option
	 * @param input
	 * @param args
	 * @return
	 */
	private static ErrorCode getFromName(Scanner input, String args) {
		NameFilter filter = NameFilter.of(args);
		List<Product> products = StoreInformation.filteredProducts(filter);
		System.out.printf("product by name[%s]:\n%s\n", args, productsToString(products));
		return ErrorCode.OK;
	}

	private static ErrorCode AddProduct(Scanner input) {
		return ErrorCode.OK;
	}

	/**
	 * Total Spent Option
	 * @param input
	 * @param cat
	 * @return
	 * @throws InvalidInputException
	 */
	private static ErrorCode totalSpent(Scanner input, String cat) throws InvalidInputException {
		if (cat.isEmpty()) {
			double totalSpent = StoreInformation.totalSpent(new TrueFilter());
			System.out.printf("totalSpent on everything:\n%s\n", totalSpent);
		} else {
			if (!cat.equalsIgnoreCase("Grocery") &&
				!cat.equalsIgnoreCase("Stationary") &&
				!cat.equalsIgnoreCase("Toiletry") &&
				!cat.equalsIgnoreCase("Vegetable")
			) {
				throw new InvalidInputException(String.format("Invalid category '%s', please try again.", cat));
			}
			double totalSpent = StoreInformation.totalSpent(CategoryFilter.of(cat));
			System.out.printf("totalSpent on '%s':\n%s\n", cat.toLowerCase(), totalSpent);
		}
		return ErrorCode.OK;
	}

	/**
	 * Profits Option
	 * @param input
	 * @param cat
	 * @return
	 * @throws InvalidInputException
	 */
	private static ErrorCode profits(Scanner input, String cat) throws InvalidInputException {
		if (!cat.equalsIgnoreCase("Grocery") &&
			!cat.equalsIgnoreCase("Stationary") &&
			!cat.equalsIgnoreCase("Toiletry") &&
			!cat.equalsIgnoreCase("Vegetable")
		) {
			throw new InvalidInputException(String.format("Invalid category '%s', please try again.", cat));
		}
		double profits = StoreInformation.profitsByCategory(cat);
		System.out.printf("profits for '%s':\n%s\n", cat.toLowerCase(), profits);
		return ErrorCode.OK;
	}

	/**
	 * Returns a string representation of a List of products. Each product is listed on a new line.
	 * @param products
	 * @return
	 */
	private static String productsToString(List<Product> products) {
		return products.stream()
					   .map(Product::toString)
					   .collect(Collectors.joining("\n"));
	}
}
