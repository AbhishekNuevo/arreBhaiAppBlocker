# ArreBhai
I created this app to help block social media applications that often lead to excessive use. I've noticed that we tend to use certain apps and then end up spending much more time than we intended, only to feel regret afterward. This is especially true with short-form content. This app 'ArreBhai' ("Arre Bhai" is an informal Hindi phrase that expresses exasperation or anguish.) can block the apps and websites which you select to block. But my biggest pain is still not solved which allowing YouTube long-form content but blocking short-form content. 
I know there are many apps that provide services to block applications but those are paid or annoying ads. My main intention was to block the short-form content which is still not solved but if you use YouTube in browser it can block.(But not working 100% as sometimes URL in search bar is not visible while scrolling short videos). 

I want to keep this open source and free. 

Why I created in React Native?-  Yes I know this can be created in Android native only but just for some learning I tried in RN. 

# Pending Tasks
1. Feature for custom app and websites to block.
2. Block short-form contents (I tried but didn't find a solution yet)
3. create similar app for IOS

# To download APK 
https://drive.google.com/file/d/1l_BdMlxJHgCr1IrX5xkgcWcH9vs4uten/view?usp=sharing

## Features WIP (Work in Progress)

- Customizable unlock times
- Block custom websites and apps.
- Multilingual support (Hindi and English)

## Getting Started

### Prerequisites

- Node.js
- React Native CLI
- Android Studio (for Android development)
- Xcode (for iOS development)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/ArreBhai.git
   cd ArreBhai
   ```

2. Install dependencies:
   ```bash
   npm install
   # or
   yarn install
   ```

3. Start the Metro bundler:
   ```bash
   npm start
   # or
   yarn start
   ```

4. Run the app:

   For Android:
   ```bash
   npm run android
   # or
   yarn android
   ```

   For iOS:
   ```bash
   npm run ios
   # or
   yarn ios
   ```

## Project Structure

- `App.tsx`: Main application component
- `src/`: Source files
  - `HomeScreen.tsx`: Home screen component
  - `SettingsScreen.tsx`: Settings screen component
  - `ApBlockScreen.tsx`: App blocking screen component
  - `blockScreen.tsx`: Block notification screen component
  - `content.ts`: Multilingual content

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License.
