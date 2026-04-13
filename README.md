# Filmstaden — Showcase Demo

A non-affiliated, for-fun concept redesign of the Swedish cinema chain's mobile app, built with modern Android tooling. 

All content, logos, and brand references are used only as a visual reference for exploration; this is not an official Filmstaden product.

The goal was to practice recent Android stack pieces (Jetpack Navigation 3, shared-element transitions, Koin 4, edge-to-edge) while building something a little more opinionated than a counter app — a small, self-contained booking flow with realistic states.

## Demo

https://github.com/jacksonmafra-umain/filmstaden/raw/main/design/demo.mp4

> If the player doesn't render inline on your client, grab the file directly: [`design/demo.mp4`](design/demo.mp4).

## Screenshots

| Home | Cinema picker | Movie Detail | Seat Selection |
| :--: | :--: | :--: | :--: |
| ![Home](screenshots/home.png) | ![Cinema selection sheet](screenshots/home-cinema-sheet.png) | ![Movie Detail](screenshots/movie-detail.png) | ![Seat Selection](screenshots/seat-selection.png) |

| Payment | My Tickets | More / Profile | Login |
| :--: | :--: | :--: | :--: |
| ![Payment sheet](screenshots/payment-sheet.png) | ![My Tickets](screenshots/my-tickets.png) | ![More](screenshots/more.png) | ![Login](screenshots/login.png) |

## What's inside

- **Home** with reactive cinema selector (shared state across screens)
- **Movie Detail** with hero image, date/time chips — shared-element transition from poster to hero
- **Seat Selection** with tiered pricing, live-derived stepper (label and price-each reflect the currently selected tier, stepper blocked at limits)
- **Payment** bottom sheet with card / Swish options
- **My Tickets** with a `HorizontalPager` of QR-code tickets
- **More / Profile** with stats and animated theme toggle
- **Login** modal

## Design source

The `design/filmstaden.pen` file is the source of truth for the mockups; screenshots above are exported from it.
