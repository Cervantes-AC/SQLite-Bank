Our project is about improving the current banking system by making it more organized, flexible, and reliable. The focus is on applying SOLID principles to keep the code clean and easy to expand without breaking things.  

The **Single Responsibility Principle** helps separate transaction logic from accounts, making the code clearer and easier to maintain. **Open-Closed Principle** ensures we can add new account types, like StudentAccount or BusinessAccount, without touching the existing ones. **Interface Segregation** ensures accounts only have the features they actually need — like transfers or loans — without unnecessary methods. **Dependency Inversion** lets us switch between file storage and databases easily, keeping storage logic independent from the rest of the system.  

For saving data, we can choose **file storage** (good for readability and debugging) or **SQLite database** (better for performance and handling multiple accounts).  

The final system should support multiple transactions at once, store data permanently, and make it easy to add new features without rewriting everything. It’s about creating a system that works now and stays flexible for the future.
