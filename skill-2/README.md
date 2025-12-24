# Hibernate CRUD Demo

This project demonstrates Hibernate CRUD operations with three ID generation strategies (AUTO, IDENTITY, SEQUENCE).

How to build and run:

1. Build:

```powershell
mvn -q package
```

2. Run demo (exec plugin):

```powershell
mvn exec:java -Dexec.mainClass=com.example.hibernate.App
```

Notes:
- Uses H2 in-memory DB (no external setup required).
- Entities: `ProductAuto`, `ProductIdentity`, `ProductSequence` in `src/main/java/com/example/hibernate/entity`.
- To push to GitHub: create a new repo and `git push` this project folder.
