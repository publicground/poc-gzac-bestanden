import {Component, OnInit} from '@angular/core';
import {KeycloakService} from 'keycloak-angular';


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    constructor(
        private keycloakService: KeycloakService
    ) {
    }
    ngOnInit() {
        this.getUserIdentity();
    }
    getUserIdentity() {
        this.keycloakService.loadUserProfile().then((profile) => {
            localStorage.setItem('userIdentity', JSON.stringify(profile));
        });
    }
}
