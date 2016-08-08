package services

import scala.collection.immutable.ListMap

import securesocial.core.providers.UsernamePasswordProvider
import securesocial.core.RuntimeEnvironment

import models.User

class Environment extends RuntimeEnvironment.Default[User] {
    override val userService: securesocial.core.services.UserService[User] =
            new UserService()

    override lazy val providers = ListMap(
        include(new UsernamePasswordProvider[User](userService, avatarService,
                viewTemplates, passwordHashers))
    )
}
