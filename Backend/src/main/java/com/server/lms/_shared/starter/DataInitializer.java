package com.server.lms._shared.starter;

import com.server.lms.book.entity.Book;
import com.server.lms.book.repository.BookRepository;
import com.server.lms.category.entity.Category;
import com.server.lms.category.repository.CategoryRepository;
import com.server.lms.loans.entity.BookLoan;
import com.server.lms.loans.enums.BookLoanState;
import com.server.lms.loans.repository.BookLoanRepository;
import com.server.lms.subscription.entity.Subscription;
import com.server.lms.subscription.entity.SubscriptionPlan;
import com.server.lms.subscription.repository.SubscriptionPlanRepository;
import com.server.lms.subscription.repository.SubscriptionRepository;
import com.server.lms.user.entity.User;
import com.server.lms.user.enums.AuthProvider;
import com.server.lms.user.enums.UserRole;
import com.server.lms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final BookLoanRepository bookLoanRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        try {
            // Check if database is empty
            if (!isDatabaseEmpty()) {
                log.info("Database is not empty. Skipping data initializing.");
                return;
            }

            log.info("Starting database initializing...");

            // Create data in proper order
            List<User> users = initUsers();
            List<SubscriptionPlan> plans = initSubscriptionPlans();
            initSubscriptions(users, plans);
            List<Category> categories = initCategories();
            List<Book> books = initBooks(categories);
            initBookLoans(users, books);

            log.info("Database initializing completed successfully!");
        } catch (Exception e) {
            log.error("Error during database initializing", e);
            throw new RuntimeException("Database initializing failed", e);
        }
    }

    private boolean isDatabaseEmpty() {
        return userRepository.count() == 0 &&
                categoryRepository.count() == 0 &&
                bookRepository.count() == 0 &&
                subscriptionPlanRepository.count() == 0;
    }

    // ==================== Users & Roles ====================
    private List<User> initUsers() {
        log.info("initializing users...");

        List<User> users = new ArrayList<>();

        // Admin User
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setName("Admin User");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setPhone("+1234567890");
        admin.setRole(UserRole.ADMIN);
        admin.setAuthProvider(AuthProvider.LOCAL);
        admin.setLastLogin(LocalDateTime.now());
        users.add(userRepository.save(admin));
        log.info("Created admin user: {}", admin.getEmail());

        // Regular Users
        String[] userNames = {
                "user1@gmail.com",
                "user2@gmail.com",
                "user3@gmail.com"
        };

        String[] userFullNames = {
                "user 1",
                "user 2",
                "user 3"
        };

        for (int i = 0; i < userNames.length; i++) {
            User user = new User();
            user.setEmail(userNames[i]);
            user.setName(userFullNames[i]);
            user.setPassword(passwordEncoder.encode("User@123"));
            user.setPhone("+123456789" + i);
            user.setRole(UserRole.USER);
            user.setAuthProvider(AuthProvider.LOCAL);
            user.setLastLogin(LocalDateTime.now().minusDays(random.nextInt(30)));
            users.add(userRepository.save(user));
            log.info("Created user: {}", user.getEmail());
        }

        return users;
    }

    // ==================== Subscription Plans ====================
    private List<SubscriptionPlan> initSubscriptionPlans() {
        log.info("initializing subscription plans...");

        List<SubscriptionPlan> plans = new ArrayList<>();

        // Basic Plan
        SubscriptionPlan basicPlan = new SubscriptionPlan();
        basicPlan.setPlanCode("BASIC");
        basicPlan.setName("Basic Plan");
        basicPlan.setDescription("Perfect for casual readers. Borrow up to 3 books with 14 days borrowing period.");
        basicPlan.setDurationDays(30);
        basicPlan.setPrice(299L); // EGP
        basicPlan.setCurrency("EGP");
        basicPlan.setMaxBookAllowed(3);
        basicPlan.setMaxDaysPerBook(14);
        basicPlan.setMaxRenewals(1);
        basicPlan.setMaxBorrowingDays(14);
        basicPlan.setDisplayOrder(1);
        basicPlan.setIsActive(true);
        basicPlan.setIsFeatured(false);
        plans.add(subscriptionPlanRepository.save(basicPlan));
        log.info("Created Basic subscription plan");

        // Premium Plan
        SubscriptionPlan premiumPlan = new SubscriptionPlan();
        premiumPlan.setPlanCode("PREMIUM");
        premiumPlan.setName("Premium Plan");
        premiumPlan.setDescription("For book lovers. Borrow up to 7 books with 21 days borrowing period and unlimited renewals.");
        premiumPlan.setDurationDays(30);
        premiumPlan.setPrice(699L);
        premiumPlan.setCurrency("EGP");
        premiumPlan.setMaxBookAllowed(7);
        premiumPlan.setMaxDaysPerBook(21);
        premiumPlan.setMaxRenewals(3);
        premiumPlan.setMaxBorrowingDays(21);
        premiumPlan.setDisplayOrder(2);
        premiumPlan.setIsActive(true);
        premiumPlan.setIsFeatured(true);
        premiumPlan.setBadgeText("Most Popular");
        plans.add(subscriptionPlanRepository.save(premiumPlan));
        log.info("Created Premium subscription plan");

        // Unlimited Plan
        SubscriptionPlan unlimitedPlan = new SubscriptionPlan();
        unlimitedPlan.setPlanCode("UNLIMITED");
        unlimitedPlan.setName("Unlimited Plan");
        unlimitedPlan.setDescription("The ultimate reading experience. Borrow unlimited books with 30 days borrowing period.");
        unlimitedPlan.setDurationDays(30);
        unlimitedPlan.setPrice(1299L);
        unlimitedPlan.setCurrency("EGP");
        unlimitedPlan.setMaxBookAllowed(15);
        unlimitedPlan.setMaxDaysPerBook(30);
        unlimitedPlan.setMaxRenewals(5);
        unlimitedPlan.setMaxBorrowingDays(30);
        unlimitedPlan.setDisplayOrder(3);
        unlimitedPlan.setIsActive(true);
        unlimitedPlan.setIsFeatured(false);
        plans.add(subscriptionPlanRepository.save(unlimitedPlan));
        log.info("Created Unlimited subscription plan");

        return plans;
    }

    // ==================== Subscriptions ====================
    private void initSubscriptions(List<User> users, List<SubscriptionPlan> plans) {
        log.info("initializing subscriptions...");

        // Admin gets Premium Plan (Active)
        Subscription adminSub = new Subscription();
        adminSub.setUser(users.get(0)); // admin
        adminSub.setSubscriptionPlan(plans.get(1)); // Premium
        adminSub.initFromPlan();
        adminSub.setStartDate(LocalDate.now().minusDays(10));
        adminSub.calculateEndDate();
        adminSub.setIsActive(true);
        adminSub.setAutoRenew(true);
        subscriptionRepository.save(adminSub);
        log.info("Created active subscription for admin");

        // User 1: Basic Plan (Active)
        Subscription user1Sub = new Subscription();
        user1Sub.setUser(users.get(1));
        user1Sub.setSubscriptionPlan(plans.get(0)); // Basic
        user1Sub.initFromPlan();
        user1Sub.setStartDate(LocalDate.now().minusDays(5));
        user1Sub.calculateEndDate();
        user1Sub.setIsActive(true);
        user1Sub.setAutoRenew(false);
        subscriptionRepository.save(user1Sub);
        log.info("Created active subscription for user 1");

        // User 1: Premium Plan (Expired/Inactive)
        Subscription user1OldSub = new Subscription();
        user1OldSub.setUser(users.get(1));
        user1OldSub.setSubscriptionPlan(plans.get(1)); // Premium
        user1OldSub.initFromPlan();
        user1OldSub.setStartDate(LocalDate.now().minusDays(45));
        user1OldSub.calculateEndDate();
        user1OldSub.setIsActive(false);
        user1OldSub.setCancelledAt(LocalDateTime.now().minusDays(10));
        user1OldSub.setCancelledReason("Plan expired");
        subscriptionRepository.save(user1OldSub);
        log.info("Created inactive subscription for user 1");

        // User 2: Unlimited Plan (Active)
        Subscription user2Sub = new Subscription();
        user2Sub.setUser(users.get(2));
        user2Sub.setSubscriptionPlan(plans.get(2)); // Unlimited
        user2Sub.initFromPlan();
        user2Sub.setStartDate(LocalDate.now());
        user2Sub.calculateEndDate();
        user2Sub.setIsActive(true);
        user2Sub.setAutoRenew(true);
        subscriptionRepository.save(user2Sub);
        log.info("Created active subscription for user 2");

        // User 3: Basic Plan (Expired)
        Subscription user3Sub = new Subscription();
        user3Sub.setUser(users.get(3));
        user3Sub.setSubscriptionPlan(plans.get(0)); // Basic
        user3Sub.initFromPlan();
        user3Sub.setStartDate(LocalDate.now().minusDays(40));
        user3Sub.calculateEndDate();
        user3Sub.setIsActive(false);
        subscriptionRepository.save(user3Sub);
        log.info("Created inactive subscription for user 3");
    }

    // ==================== Categories ====================
    private List<Category> initCategories() {
        log.info("initializing categories...");

        List<Category> categories = new ArrayList<>();

        // Main Categories
        String[] categoryNames = {"Fiction", "Science", "Technology", "History", "Biography"};
        String[] categoryCodes = {"FICTION", "SCIENCE", "TECH", "HISTORY", "BIOGRAPHY"};

        for (int i = 0; i < categoryNames.length; i++) {
            Category category = new Category();
            category.setName(categoryNames[i]);
            category.setCode(categoryCodes[i]);
            category.setDescription("Collection of " + categoryNames[i] + " books");
            category.setDisplayOrder(i + 1);
            category.setIsActive(true);
            categories.add(categoryRepository.save(category));
            log.info("Created category: {}", categoryNames[i]);
        }

        // Sub-categories for Fiction
        Category fictionSubCategory = new Category();
        fictionSubCategory.setName("Science Fiction");
        fictionSubCategory.setCode("SCI_FI");
        fictionSubCategory.setDescription("Science fiction and futuristic stories");
        fictionSubCategory.setParentCategory(categories.get(0)); // Fiction
        fictionSubCategory.setDisplayOrder(1);
        fictionSubCategory.setIsActive(true);
        categoryRepository.save(fictionSubCategory);
        log.info("Created sub-category: Science Fiction");

        return categories;
    }

    // ==================== Books ====================
    private List<Book> initBooks(List<Category> categories) {
        log.info("initializing books...");

        List<Book> books = new ArrayList<>();

        // Real book data
        Object[][] bookData = {
                // Fiction Books
                {"978-0062315007", "The Great Gatsby", "F. Scott Fitzgerald", "Scribner", LocalDate.of(2004, 5, 1), "English", 180, "A masterpiece of American literature set in the Jazz Age."},
                {"978-0451524935", "1984", "George Orwell", "Signet Classics", LocalDate.of(1950, 6, 8), "English", 328, "A dystopian novel depicting a totalitarian society."},
                {"978-0141439570", "Pride and Prejudice", "Jane Austen", "Penguin Classics", LocalDate.of(1813, 1, 28), "English", 432, "A romantic novel of manners and marriage."},

                // Science Books
                {"978-0071393621", "A Brief History of Time", "Stephen Hawking", "Bantam", LocalDate.of(1988, 4, 1), "English", 256, "An exploration of the universe from the Big Bang to black holes."},
                {"978-0062316097", "Sapiens", "Yuval Noah Harari", "HarperCollins", LocalDate.of(2011, 1, 1), "English", 544, "A history of humankind from the Stone Age to the present."},

                // Technology Books
                {"978-0596007126", "Head First Design Patterns", "Freeman & Freeman", "O'Reilly Media", LocalDate.of(2004, 10, 1), "English", 688, "Learn design patterns through memorable visuals and easy-to-follow explanations."},
                {"978-0134685991", "Effective Java", "Joshua Bloch", "Addison-Wesley", LocalDate.of(2018, 12, 4), "English", 416, "Programming language guide with best practices and techniques."},

                // History Books
                {"978-0143039593", "Guns, Germs, and Steel", "Jared Diamond", "W. W. Norton", LocalDate.of(1997, 4, 1), "English", 496, "Explores how geography shapes civilizations."},
                {"978-0375413490", "The History of the Ancient World", "Susan Wise Bauer", "W. W. Norton", LocalDate.of(2007, 3, 1), "English", 912, "A comprehensive guide through ancient history."},

                // Biography Books
                {"978-0805069389", "Steve Jobs", "Walter Isaacson", "Simon & Schuster", LocalDate.of(2011, 10, 24), "English", 656, "The exclusive biography of the Apple founder."},
                {"978-0310332770", "Becoming", "Michelle Obama", "Crown", LocalDate.of(2018, 11, 13), "English", 560, "Memoirs of the former First Lady of the United States."}
        };

        for (int i = 0; i < bookData.length; i++) {
            String isbn = (String) bookData[i][0];
            String title = (String) bookData[i][1];
            String author = (String) bookData[i][2];
            String publisher = (String) bookData[i][3];
            LocalDate pubDate = (LocalDate) bookData[i][4];
            String language = (String) bookData[i][5];
            Integer pages = (Integer) bookData[i][6];
            String description = (String) bookData[i][7];

            Book book = new Book();
            book.setIsbn(isbn);
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setPublicationDate(pubDate);
            book.setLanguage(language);
            book.setPages(pages);
            book.setDescription(description);
            book.setPrice(BigDecimal.valueOf(50 + random.nextInt(200)));
            book.setTotalCopies(random.nextInt(10) + 3); // 3-12 copies
            book.setAvailableCopies(random.nextInt(book.getTotalCopies()) + 1);
            book.setIsActive(true);

            // Assign categories
            int categoryIndex = i % categories.size();
            book.setCategory(categories.get(categoryIndex));

            // Generate realistic cover image URL
            book.setCoverImageUrl("https://covers.openlibrary.org/b/id/" + (9000000 + i) + "-M.jpg");

            books.add(bookRepository.save(book));
            log.info("Created book: {} by {}", title, author);
        }

        return books;
    }

    // ==================== Book Loans ====================
    private void initBookLoans(List<User> users, List<Book> books) {
        log.info("initializing book loans...");

        // Skip admin user for loans
        List<User> borrowers = users.subList(1, users.size());

        // Create loans with different states
        BookLoanState[] loanStates = {BookLoanState.BORROWED, BookLoanState.RETURNED, BookLoanState.OVERDUE};

        for (int i = 0; i < 8; i++) {
            User user = borrowers.get(i % borrowers.size());
            Book book = books.get(i % books.size());

            BookLoanState state = loanStates[i % loanStates.length];

            LocalDate borrowDate = LocalDate.now().minusDays(random.nextInt(40));
            LocalDate dueDate = borrowDate.plusDays(14);
            LocalDate returnDate;

            if (state == BookLoanState.RETURNED) {
                returnDate = dueDate.minusDays(random.nextInt(5));
            } else if (state == BookLoanState.OVERDUE) {
                returnDate = LocalDate.now(); // Not returned yet
            } else { // BORROWED
                returnDate = LocalDate.now(); // Will be updated
            }

            BookLoan loan = BookLoan.builder()
                    .user(user)
                    .book(book)
                    .bookLoanState(state)
                    .borrowingDate(borrowDate)
                    .dueDate(dueDate)
                    .returnDate(returnDate)
                    .renewalCount(state == BookLoanState.RENEWED ? 1 : 0)
                    .maxRenewals(2)
                    .isOverdue(state == BookLoanState.OVERDUE)
                    .overdueDays(state == BookLoanState.OVERDUE ? (int) (LocalDate.now().toEpochDay() - dueDate.toEpochDay()) : 0)
                    .notes("Sample loan record")
                    .build();

            bookLoanRepository.save(loan);
            log.info("Created {} book loan for user {} on book {}", state, user.getEmail(), book.getTitle());
        }

        log.info("Created {} book loans", 8);
    }


}

