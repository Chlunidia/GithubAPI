# GitHub User App
GitHub User is an Android application developed in Kotlin that integrates the GitHub API to allow users to search for and view GitHub user profiles. The app provides detailed information about users, including their followers and following lists. Additionally, it supports user favorites and theme customization.

## Preview
<div align="center">
  <img src="https://github.com/Chlunidia/github-api/assets/115222445/fef7933f-0978-46c7-bbbb-12d1c14113bd" alt="Example Image">
</div>

## Features
- Displays a list of users from the GitHub API using `RecyclerView`. Each item in the list shows the user's avatar and username.
- Users can search for other GitHub users, with search results fetched from the GitHub API.
- Users can view detailed information of any user from the search results.The detail page shows:
  - Avatar
  - Username
  - Full Name
  - Number of Followers
  - Number of Following
- Displays the user's followers and following lists fetched from the GitHub API using fragments.
- Users can add or remove users from their favorites list.
- A dedicated page to view the list of favorite users.
- Users can view details of users in their favorites list.
- Allows users to switch between light and dark themes.

## Requirements

- Android Studio
- Kotlin
- GitHub API

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/Chlunidia/github-api.git
2. Open the project in Android Studio.
3. Build and run the project on an Android device or emulator.
