David Mesquita
CSC371 Mobile Application Development
Semester-Long Capstone Project Documentation - Bird Buddy

Introduction:
My Semester-Long project is an application called Bird Buddy. Bird Buddy is an educational mobile application designed to help younger kids identify common birds they encounter. The app is designed to encourage curiosity and outdoor engagement as opposed to the more standard short-form digital content that is currently out in children's spaces such as TikTok and Youtube Shorts.

Summary Of Application Features:

<img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/c9e97835-7e1c-46ee-8078-da5fd42dee84" /> <img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/2c341b0b-70a9-4312-b739-439cef9954a9" />

When opening the app the user is prompted to either login or open an account, The program uses Room-based persistent storage for all user accounts and data, data is saved per user.

<img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/f22511cc-636b-4f27-8ad0-b9e37c44b2ef" />


Once inside the app the user is faced with three options, to identify a bird, view their collection and see all birds listed on the app.

<img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/aa337e83-fbb4-42a2-8c0a-ffa07a4b0946" />

The process of identifying a bird is very simple as it is designed for children, a user can answer the following questions to narrow down which bird it could possibly be

<img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/87afc9d5-e66c-487a-b324-b0d945b07d4b" />

In this example I input a medium sized, blue bird, that I found in my backyard and the app displays Blue Jay as the likely bird I saw, from here the application not only shows a picture of the bird to help a user identify it but also allows a user to upload their own picture of the bird and save it to their collection.

<img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/f923e024-4686-49a4-b548-d1893417a4ab" /> <img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/3073f5a4-1dfe-4738-8913-2bb1890b4de0" /><img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/c6cd98c4-4829-4ff5-bb79-52a0ffc6cd65" />

If a user uploads a photo it will override the apps default in their collection, if they do not upload a photo their collection will show the default bird picture an will give an option to add one later, each bird also comes with a fun fact for kids to help educate them on the bird they found!

<img width="270" height="600" alt="image" src="https://github.com/user-attachments/assets/bbe06ebb-dd19-4955-81fa-3b5d10d9e7a4" />

The Third option on the app shows all the birds currently added in the database that the app can recognize, at this point in time there are only 12 birds that are common to New York that are hard coded in.

Screenshots of Tablet:
	I made sure the application also had a child friendly interface on the tablet as it is very common for a child's first electronic device to be some form of tablet: 
  
<img width="640" height="400" alt="image" src="https://github.com/user-attachments/assets/f7686573-4960-4943-9648-1572f6686760" />
<img width="640" height="400" alt="image" src="https://github.com/user-attachments/assets/18624569-3622-48c1-afb2-15edca26d62d" />
<img width="640" height="400" alt="image" src="https://github.com/user-attachments/assets/47f1c0b1-8a74-4dcc-8ebd-141ced913aa1" />
<img width="640" height="400" alt="image" src="https://github.com/user-attachments/assets/e8d52be4-e067-4334-88a6-ef967e9dc0c1" />
<img width="640" height="400" alt="image" src="https://github.com/user-attachments/assets/c7ad6d94-aa64-441c-b171-6c4b38360033" />
<img width="640" height="400" alt="image" src="https://github.com/user-attachments/assets/fdcdec2c-03a8-46e4-a8c4-78e1b982ca38" />

Technical Implementation: 
Bird Buddy is built using Jetpack Compose for UI and Room for persistent local storage.
The application follows an MVVM structure:

ViewModel — holds app state, handles business logic, communicates with the repository
Repository — abstracts Room database operations
Room Entities & DAOs — Users, Birds, UserBird relationships
Navigation — Jetpack Compose Navigation
Image Persistence — URI permissions + stored image paths for uploaded kid photos

Development Process & Key Challenges:
	The project began with designing a simple bird identification flow that children could easily understand. After establishing the core UI, Room was implemented to store users and their bird collections. I had a bit of trouble making the user's updated photos persistent when the app closed, and making sure everything looked okay on the tablet side of things as well. After a couple of rounds of testing and refining the UI the end result was what I wanted, visually polished and child friendly.


Conclusion: 
	Overall I am very satisfied with how the application turned out. I aimed to make an application designed to encourage more engaging activities for children and I feel at least in concept I have achieved that. I wanted to make the UI child friendly utilizing large buttons, minimal text, and a simple layout to remove as much complexity as possible to increase its accessibility. The app encourages nature based engagement, self driven discovery and real world photo taking that can help children interact with and gain curiosity for the world around them.
  
  https://github.com/dmesqu/davidmesquita_csc371_final_project_birdbuddy
