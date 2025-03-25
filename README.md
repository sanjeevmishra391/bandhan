# Bandhan

An application that helps party plot owner to keep track of bookings, events, users, revenue and expenses.

## Requirements
### Admin Dashboard:
1. Can see bookings (customer, price, date, events, )
2. Booking can be done
3. Track the history and prices
4. Update facilities, events, prices

### User:
1. Can see their booking details > facilities, events, total amount, paid and dues
2. Explore pricing, events, facilities

## Models
### User
Stores user information
- id
- firstname
- lastname
- address
- mobile
- email (optional)
- user_role {CUSTOMER, ADMIN}
- creation_date
- last_update_date

### Event_Category
Stores the different categories of the event. Categories could be Wedding, Reception, Stay, Party etc

- id
- name
- sub_category
- description
- creation_date
- last_update_date

### Event
Events such as Wedding, Reception, Stay, Party {Birthday, Anniversary, Others}
- id
- name
- description
- category_id
- base_price 
- status {ACTIVE, INACTIVE}
- creation_date
- last_update_date

### Facility
Facilities are options which user can opt-in or opt-out. such as Decoration, Food, Catering, Air Conditioning
These will incur additional costs.

**Decoration** : A florist/gardener will look over decoration and may have few people to help. We would lend some items to him.  
**Food** : We could provide food facility such as cook and helpers.  
**Catering** : Waiters and caterers.   
**Air Conditioning** : If opted then charge more for ac.  

Few facilities have no facility head/members. While in few facilities we need to provide items either from inventory or from other sources.

One facility could be provided by many providers
- id
- name
- description
- creation_date
- last_update_date

### Facility_Provider
- id
- name
- contact
- facility_id
- creation_date
- last_update_date

### Booking
User booked for an event opting for some facilities and paid a price of the event. 
- id
- event_id
- user_id
- agreed_price
- status {CONFIRMED, CANCELLED, COMPLETED}
- creation_date
- last_update_date

There could be more charges for facilities such as electricity, ac.

### Booking_Facility
Facilities opted for a booking
- booking_id
- facility_id
- facility_cost
- facility_description
- creation_date
- last_update_date

### Calender_Dates
- calendar_date
- status {BLOCKED, PARTIALLY_BOOKED, FULLY_BOOKED}
- description

### Booking_Dates
- booking_id
- calendar_date
- start_time
- end_time

### Charges
Charges incurred such as electricity, damage, additional
- id
- booking_id
- type {ELECTRICITY, DAMAGE, FACILITY, ADDITIONAL}
- status {PENDING, PAID, REFUNDED}
- description
- amount
- creation_date
- last_update_date

### Bill
Amount paid by customer each time for the booking.
- id
- booking_id
- amount
- description {AGREED_PRICE, FACILITY, ELECTRICITY, ADDITIONAL}
- date
- receiver_name
- receiver_contact
- payment_type {CASH, UPI, DEBIT CARD, BANK DEPOSIT}
- status {PAID, PENDING, PARTIAL}
- reference_id
- creation_date
- last_update_date

### Expenses
Expenses track the expenses of marriage hall. Not specific to booking
**Cleaning** : After each event we have to clean the place which incur cost. Cleaner needs to be provided items.  
**Management** : Those who manage the event and makes sure everything is working smoothly.  
**Security** : Guards are needs for security.  
- id
- bookingId {nullable}
- type {CLEANING, SECURITY, DAMAGE, PAINT ...}
- description
- amount
- date
- creation_date
- last_update_date

### Inventory
Maintains track of items in the inventory
- id
- item
- description
- quantity
- creation_date
- last_update_date

Entries for below items and more

- chair, sofa, sofa_cover, marriage_sofa, round_table, rect_table
- carpet, tent, fog_light
- plate, spoons, kadhai ...

### How the logic works?
When a user books for an event, entry goes into booking table. He can also opt for some additional facility apart from the facilities included in the package. User is offered some price but he agrees at price. The additional facilities and electricity will be additional charges which will be inserted into charges table. Now customer can pay all in one payment or may take few payments to make the payment. For each payment entry is inserted into bill table. Expenses table store the expense incurred by the marriage hall and inventory store the item details which are present in inventory.

## Services
1. Booking service:
    getBooking(), getAllBookings(), addBooking(), updateBooking(), cancelBooking()
2. Calender service:
    getCalendarDates(), findDates(), findDatesByCriteria()
3. User service:
    signUp(), login(), getUser(), getAllUsers(), updateUsersById(), deleteUserById()
4. Events service:
    getEvent(), addEvent(), deleteEvent()
5. Facility service:
    getFacility(), addFacility(), deleteFacility()
6. Inventory service:
    getItem(), getAllItems(), addItem(), updateItem(), deleteItem()
