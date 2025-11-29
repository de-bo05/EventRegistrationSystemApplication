# How to Run Database Schema

## Method 1: MySQL Command Line (Recommended)

### Step 1: Open Command Prompt or PowerShell
Navigate to the project directory:
```powershell
cd C:\Users\ajibe\OneDrive\Desktop\event-registration-system\database
```

### Step 2: Run the script
```bash
mysql -u root -p < schema.sql
```

You'll be prompted for your MySQL root password.

**OR** if MySQL is not in your PATH:

```bash
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p < schema.sql
```

## Method 2: MySQL Command Line (Interactive)

1. Open Command Prompt/PowerShell
2. Connect to MySQL:
   ```bash
   mysql -u root -p
   ```
3. Once connected, run:
   ```sql
   source C:\Users\ajibe\OneDrive\Desktop\event-registration-system\database\schema.sql
   ```
   Or if you're in the database folder:
   ```sql
   source schema.sql
   ```

## Method 3: MySQL Workbench (GUI)

1. Open **MySQL Workbench**
2. Connect to your MySQL server
3. Click on **File** → **Open SQL Script**
4. Navigate to: `C:\Users\ajibe\OneDrive\Desktop\event-registration-system\database\schema.sql`
5. Click **Open**
6. Click the **Execute** button (⚡ lightning bolt icon) or press `Ctrl+Shift+Enter`

## Method 4: phpMyAdmin (Web Interface)

1. Open phpMyAdmin in your browser (usually `http://localhost/phpmyadmin`)
2. Click on **SQL** tab
3. Click **Choose File** and select `schema.sql`
4. Click **Go** to execute

## Method 5: Copy-Paste in MySQL Client

1. Open the `schema.sql` file in a text editor
2. Copy all contents
3. Open MySQL command line or Workbench
4. Paste and execute

## Verify It Worked

After running the script, verify the database was created:

```sql
SHOW DATABASES;
-- Should see: event_registration_db

USE event_registration_db;
SHOW TABLES;
-- Should see: users, events, registrations, attendances

SELECT * FROM users;
-- Should see the admin user
```

## Troubleshooting

**"mysql: command not found"**
- Add MySQL bin folder to your PATH, OR
- Use full path: `"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"`

**"Access denied"**
- Make sure you're using the correct MySQL root password
- Try: `mysql -u root -p` first to test connection

**"Database already exists"**
- That's fine! The script uses `CREATE DATABASE IF NOT EXISTS`
- It will skip creation if it exists

**"Table already exists"**
- The script uses `CREATE TABLE IF NOT EXISTS`
- It's safe to run multiple times

