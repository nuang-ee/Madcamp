# Madcamp - Week 1

This is the resulting Android application, which is developed within week 1 of KAIST Madcamp.

The goal is to develop a app with 3 tabs with different functionalities.

#### The initialization of the app.
- This app needs permission to access internet, local contacts, and User gallery.
So at the very first launch of this app, the permissions mentioned above is required to the user.

#### The functionalities of each tabs
* Tab 1 : Contacts
- Fetches user's local device's contacts, and shows the name, and phone number, email address.
- Used SwipeRefresh feature to refresh the lists of contacts in a simple, intuitive way.

* Tab 2 : Gallery
- Shows an local gallery photos in a 3xN tile views.
- Used SwipeRefresh to refresh the lists of photos, to be synchronized with the device.

* Tab 3 : Weather & Fine Dust Statistics.
- Shows several weather informations, and fine dust informations.
- Find Dust informations are displayed with an emoji, to show how severe the fine dust density is. The emojis change by the density.