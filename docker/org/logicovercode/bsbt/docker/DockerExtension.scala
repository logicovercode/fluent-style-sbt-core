package org.logicovercode.bsbt.docker

import org.logicovercode.bsbt.docker.model._
import sbt.Def

trait DockerExtension {

  type DcDef = DockerContainerDefinition

  implicit def dockerContainerDefinitionToIDockerService(dcDef: DcDef): IDockerService = {
    new IDockerService {
      override def instance(): DockerService = DockerService(Seq(dcDef))

      override def sbtSettings(): Set[Def.Setting[_]] = Set()
    }
  }

  implicit def dockerContainerDefinitionTupleToIDockerService(dcDefs: (DcDef, DcDef)): IDockerService = {
    new IDockerService {
      override def instance(): DockerService = DockerService(Seq(dcDefs._1, dcDefs._2))

      override def sbtSettings(): Set[Def.Setting[_]] = Set()
    }
  }

  implicit def dockerContainerDefinitionTupleToIDockerService(dcDefs: (DcDef, DcDef, DcDef)): IDockerService = {
    new IDockerService {
      override def instance(): DockerService = DockerService(Seq(dcDefs._1, dcDefs._2, dcDefs._3))

      override def sbtSettings(): Set[Def.Setting[_]] = Set()
    }
  }

  implicit def dockerContainerDefinitionTupleToIDockerService(dcDefs: (DcDef, DcDef, DcDef, DcDef)): IDockerService = {
    new IDockerService {
      override def instance(): DockerService =     DockerService(
        Seq(dcDefs._1, dcDefs._2, dcDefs._3, dcDefs._4)
      )

      override def sbtSettings(): Set[Def.Setting[_]] = Set()
    }
  }

  implicit def dockerContainerDefinitionTupleToIDockerService(dcDefs: (DcDef, DcDef, DcDef, DcDef, DcDef)): IDockerService = {
    new IDockerService {
      override def instance(): DockerService =     DockerService(
        Seq(dcDefs._1, dcDefs._2, dcDefs._3, dcDefs._4, dcDefs._5)
      )

      override def sbtSettings(): Set[Def.Setting[_]] = Set()
    }

  }

  implicit def dockerContainerDefinitionTupleToIDockerService(dcDefs: (DcDef, DcDef, DcDef, DcDef, DcDef, DcDef)): IDockerService = {
    new IDockerService {
      override def instance(): DockerService = DockerService(
        Seq(dcDefs._1, dcDefs._2, dcDefs._3, dcDefs._4, dcDefs._5, dcDefs._6)
      )

      override def sbtSettings(): Set[Def.Setting[_]] = Set()
    }

  }

  implicit def dockerContainerDefinitionTupleToIDockerService(dcDefs: (DcDef, DcDef, DcDef, DcDef, DcDef, DcDef, DcDef)): IDockerService = {
    new IDockerService {
      override def instance(): DockerService =     DockerService(
        Seq(dcDefs._1, dcDefs._2, dcDefs._3, dcDefs._4, dcDefs._5, dcDefs._6, dcDefs._7)
      )

      override def sbtSettings(): Set[Def.Setting[_]] = Set()
    }

  }
}